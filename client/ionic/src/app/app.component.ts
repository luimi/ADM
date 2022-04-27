import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AlertController } from '@ionic/angular';
import { DeviceService, WebsocketService } from 'adm-core';
@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
})
export class AppComponent {
  isVerifying = false;
  public labels = ['Family', 'Friends', 'Notes', 'Work', 'Travel', 'Reminders'];
  constructor(public websocketCtrl: WebsocketService, public deviceCtrl: DeviceService, private alertCtrl: AlertController, private router: Router) { }

  async connect(ip: string) {
    if (this.isVerifying) {
      return
    }
    this.isVerifying = true;
    try {
      let device: any = await this.websocketCtrl.verify(ip);
      this.router.navigateByUrl('/device/'+device.ip)
    } catch (e) {
      const alert = await this.alertCtrl.create({
        header: 'Connection Error',
        message: 'WebSocket connection to '+ip+' failed',
        buttons: ['OK']
      });
      await alert.present();
    }
    this.isVerifying = false;
  }
}
