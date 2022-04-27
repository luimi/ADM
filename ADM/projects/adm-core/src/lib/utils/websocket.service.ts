import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Device } from '../models/device.model';
import { DeviceService } from './device.service';

@Injectable()
export class WebsocketService {
    private current?: WebSocket;
    isConnected = false;
    isConnecting = false;
    isSecure = false;
    status: 'Not Connected' | 'Connected' | 'Connecting' = 'Not Connected';
    verificationTimeout = 60000;
    constructor(private deviceCtrl: DeviceService) { }

    verify(url: string) {
        return new Promise((res, rej) => {
            let ws = new WebSocket(`${this.isSecure ? 'wss' : 'ws'}://${url}`);
            let timeOut: any;
            ws.onopen = () => {
                timeOut = setTimeout(() => {
                    rej({ success: false, error: 'Timeout' });
                }, this.verificationTimeout);
            };
            ws.onerror = (evt) => {
                rej({ success: false, error: evt });
            };
            ws.onmessage = (msg) => {
                try {
                    const device: Device = Object.assign(new Device, JSON.parse(msg.data));
                    this.deviceCtrl.addDevice(device);
                    ws.close();
                    res(device);
                    clearTimeout(timeOut);
                } catch (e) {
                    rej({ success: false, error: e });
                    ws.close();
                }
            };
        });
    }
    connect(device: Device): Observable<any> {
        this.disconnect();
        this.current = new WebSocket(
            `${this.isSecure ? 'wss' : 'ws'}://${device.ip}${device.port ? ':' : ''}${device.port ? device.port : ''
            }`
        );
        this.current.onerror = () => {
            this.isConnecting = false;
            this.status = 'Not Connected';
            console.log('onerror');
        };

        this.current.onopen = () => {
            this.isConnected = true;
            this.isConnecting = false;
            this.status = 'Connected';
        };
        this.current.onclose = () => {
            this.isConnected = false;
            this.status = 'Not Connected';
            console.log('onclose');
        };
        this.isConnecting = true;
        this.status = 'Connecting';
        return Observable.create((observer: any) => {
            if (this.current)
                this.current.onmessage = (msg) => {
                    try {
                        const data = JSON.parse(msg.data);
                        switch (data.type) {
                            case 'device': {
                                let device = Object.assign(new Device, data)
                                this.deviceCtrl.addDevice(device)
                            }
                        }
                        observer.next(data);
                    } catch (e) { }
                };
        });
    }
    disconnect() {
        if (this.current) {
            this.current.close();
            delete this.current;
        }
    }
    send(event: any) {
        if (this.current) {
            this.current.send(JSON.stringify(event));
        }
    }
}
