import { Component } from '@angular/core';
import { Device } from 'projects/adm-core/src/lib/models/device.model';
import { DPIs, TimeOuts } from 'projects/adm-core/src/lib/utils/constants';
import { DeviceService } from 'projects/adm-core/src/lib/utils/device.service';
import { WebsocketService } from 'projects/adm-core/src/lib/utils/websocket.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  host: string = '';
  dpis = DPIs;
  timeouts = TimeOuts;
  device: Device = new Device();
  constructor(
    public websocketCtrl: WebsocketService,
    public deviceCtrl: DeviceService
  ) { 
    this.device = deviceCtrl.getDevice(this.deviceCtrl.devices[0].id);
  }
  async verify() {
    try {
      let device = await this.websocketCtrl.verify(this.host);
      console.log(device);
    } catch (e) {
      console.log(e);
    }
  }
  connect(id: string) {
    let device: Device = this.deviceCtrl.getDevice(id)
    this.websocketCtrl.connect(device).subscribe((data) => {
      console.log('subscribe', data);
    });
  }
  remove(id: string) {
    this.websocketCtrl.disconnect();
    let device: Device = this.deviceCtrl.getDevice(id)
    this.deviceCtrl.removeDevice(device);
  }
  ngOnDestroy() {
    this.websocketCtrl.disconnect();
  }
}
