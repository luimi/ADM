import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertController } from '@ionic/angular';
import { Device, DeviceService, DPIs, TimeOuts, WebsocketService } from 'adm-core';

@Component({
  selector: 'app-device',
  templateUrl: './device.page.html',
  styleUrls: ['./device.page.scss'],
})
export class DevicePage implements OnInit {
  device: Device;
  information = [];
  DPIList = DPIs;
  TimeSleepList = TimeOuts;
  constructor(private activatedRoute: ActivatedRoute, private deviceCtrl: DeviceService, public websocketCtrl: WebsocketService, private alertCtrl: AlertController, private router: Router) { }

  ngOnInit() {
    let id = this.activatedRoute.snapshot.paramMap.get('id');
    this.device = this.deviceCtrl.getDevice(id);
    this.connectDevice();
    
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
  send(event: any){
    this.websocketCtrl.send(event)
  }
  disableEvent(){
    return !this.websocketCtrl.isConnected;
  }
  connectDevice(){
    this.websocketCtrl.connect(this.device).subscribe((data) => {
      if(data.type === 'device'){
        this.device = data;
      }
    });
    this.getInformation();
  }
  async removeDevice(){
    const alert = await this.alertCtrl.create({
      header: 'Remove Device',
      message: 'Do you want to remove this device?',
      buttons: [
        {
          text: 'No',
          role: 'cancel'
        }, {
          text: 'Yes',
          id: 'confirm-button',
          handler: () => {
            this.deviceCtrl.removeDevice(this.device);
            this.router.navigateByUrl('/main');
          }
        }
      ]
    });

    await alert.present();
  }
}
