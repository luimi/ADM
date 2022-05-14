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
  device!: Device;
  constructor(
    public websocketCtrl: WebsocketService,
    public deviceCtrl: DeviceService
  ) { 
    //this.device = deviceCtrl.devices[0];
  }
  connect(device: Device) {
    this.websocketCtrl.connect(device).subscribe((data) => {
      console.log('subscribe', data);
      this.device = data;
    });
  }
  ngOnDestroy() {
    this.websocketCtrl.disconnect();
  }

  onCheckSuccess(device: Device){
    console.log("onSuccess",device);
    this.connect(device);
  }
  onCheckFail(error: any){
    console.log("onFail",error);
  }
  onSelectedDevice(device: Device){
    console.log("onSelected", device);
    this.connect(device);
  }
  send(event: any){
    console.log(event);
    this.websocketCtrl.send(event);
  }
  isConnected(){
    return this.websocketCtrl.isConnected;
  }

}
