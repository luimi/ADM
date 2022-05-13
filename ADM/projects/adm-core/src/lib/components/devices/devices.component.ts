import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Device } from '../../models/device.model';
import { DeviceService } from '../../utils/device.service';

@Component({
  selector: 'device-list',
  templateUrl: './devices.component.html',
  styleUrls: ['./devices.component.scss']
})
export class DevicesComponent implements OnInit {
  @Output() onSelect: EventEmitter<Device> = new EventEmitter();
  constructor(public deviceCtrl: DeviceService) { }

  ngOnInit(): void {
  }
  select(device: Device){
    this.onSelect.emit(device);
  }
}
