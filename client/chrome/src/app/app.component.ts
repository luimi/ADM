import { Component } from '@angular/core';
import { Device, DeviceService, DPIs, TimeOuts, WebsocketService } from 'adm-core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'chrome';
  isVerifying = false;
  device: any;
  information: any;
  DPIList = DPIs;
  TimeSleepList = TimeOuts;
  constructor(public websocketCtrl: WebsocketService, public deviceCtrl: DeviceService) { }

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
  connect(device: Device){
    this.device = device;
    this.websocketCtrl.connect(this.device).subscribe((data) => {
      if(data.type === 'device'){
        this.device = data;
      }
    });
  }
  back(){
    this.websocketCtrl.disconnect();
    this.device = undefined;
  }
  send(event: any){
    this.websocketCtrl.send(event)
  }
  isConnected(){
    return this.websocketCtrl.isConnected;
  }
  disableEvent(){
    return !this.websocketCtrl.isConnected;
  }
  remove(){
    this.websocketCtrl.disconnect();
    this.deviceCtrl.removeDevice(this.device);
    this.device = undefined;
  }
  reconnect(){
    this.connect(this.device);
  }
}
