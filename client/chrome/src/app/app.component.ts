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
  async connect(ip: string) {
    console.log("connect to:",ip)
    if (this.isVerifying) {
      return
    }
    this.isVerifying = true;
    try {
      let device: any = await this.websocketCtrl.verify(ip);
      this.device = device;
      console.log("connect","success");
      //this.router.navigateByUrl('/device/'+device.ip)
    } catch (e) {
      console.log("connect","fail");
      //Error
    }
    this.isVerifying = false;
  }
  selectDevice(device: Device){
    this.device = device;
    this.websocketCtrl.connect(this.device).subscribe((data) => {
      if(data.type === 'device'){
        this.device = data;
      }
    });
    this.getInformation();
  }
  getInformation(){
    let info = [];
    info.push({title:'IP',description:this.device.ip});
    info.push({title:'Manufacter',description:this.device.manufacter});
    info.push({title:'Model',description:this.device.model});
    info.push({title:'Version',description:this.device.version});
    info.push({title:'SDK',description:this.device.versionSDK});
    info.push({title:'Processor',description:this.device.processor});
    this.information = info;
  }
  back(){
    this.websocketCtrl.disconnect();
    this.device = undefined;
  }
  send(event: any){
    this.websocketCtrl.send(event)
  }
  disableEvent(){
    return !this.websocketCtrl.isConnected;
  }
}
