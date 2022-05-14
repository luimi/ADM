import { Component, OnInit } from '@angular/core';
import { DeviceComponent } from '../device/device.component';

@Component({
  selector: 'device-os',
  templateUrl: './os.component.html',
  styleUrls: ['./os.component.scss']
})
export class OperativeSystemComponent extends DeviceComponent implements OnInit {

  constructor() {
    super();
  }

  ngOnInit(): void {

  }

  shutdown() {
    this.send({ type: 'shutdown' });
  }
  reboot() {
    this.send({ type: 'reboot' });
  }
  setadbkey(adbkey: String) {
    if (!adbkey || adbkey.trim() == "") return;
    this.send({ type: 'setadbkey', value1: adbkey });
  }
  startBluetooth() {
    this.send({type:'startbluetooth'});
  }
  stopBluetooth() {
    this.send({type:'stopbluetooth'});
  }
}
