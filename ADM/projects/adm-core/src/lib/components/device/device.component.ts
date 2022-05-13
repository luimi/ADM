import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { WebsocketService } from 'adm-core';
import { Device } from '../../models/device.model';

@Component({
  selector: 'device',
  templateUrl: './device.component.html',
  styleUrls: ['./device.component.scss']
})
export class DeviceComponent {
  @Input() device : Device = new Device();
  @Input() isConnected: Boolean = false;
  @Output() eventSend : EventEmitter<any> = new EventEmitter();
  constructor() { }
  send(event: any){
    this.eventSend.emit(event);
  }
}
