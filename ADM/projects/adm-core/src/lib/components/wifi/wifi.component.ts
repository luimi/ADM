import { Component, OnInit } from '@angular/core';
import { DeviceComponent } from '../device/device.component';

@Component({
    selector: 'device-wifi',
    templateUrl: './wifi.component.html',
    styleUrls: ['./wifi.component.scss']
})
export class WifiComponent extends DeviceComponent implements OnInit {

    constructor() {
        super();
    }

    ngOnInit(): void {
    }
    startADB() {
        this.send({ type: 'startadbwifi' });
    }
    stopADB() {
        this.send({ type: 'stopadbwifi' })
    }
    saveNetwork(name: String, password: String) {
        if (!name || !password) return;
        this.send({ type: 'setwifi', value1: name, value2: password });
    }
    removeNetwork(id: String){
        if(!this.isConnected) return;
        this.send({type:'removewifi', value1: id})
    }
}
