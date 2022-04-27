import { Injectable } from '@angular/core';
import { Device } from '../models/device.model';

@Injectable()
export class DeviceService {
  devices: Device[] = [];
  constructor() {
    let _devices: any[] = JSON.parse(localStorage.getItem('devices') || "[]")
    _devices.forEach(device => {
      this.devices.push(Object.assign(new Device, device));
    });
  }
  addDevice(device: Device) {
    let index = this.indexOfDevice(device);
    if (index >= 0) {
      this.devices[index] = device;
    } else {
      this.devices.push(device);
    }
    this.save();
  }
  removeDevice(device: Device) {
    let index = this.indexOfDevice(device);
    if (index >= 0) {
      this.devices.splice(index, 1);
      this.save();
    }
  }
  getDevice(id: string): Device{
    let index = this.indexOfDeviceByID(id);
    let device = this.devices[0];
    if(index >= 0){
      device = this.devices[index];
    }
    return device
  }
  save() {
    let devices = JSON.stringify(this.devices);
    localStorage.setItem('devices', devices);
  }
  indexOfDevice(device: Device) {
    return this.devices
      .map((o) => {
        return o.getUID();
      })
      .indexOf(device.getUID());
  }
  indexOfDeviceByID(id: string) {
    return this.devices
      .map((o) => {
        return o.getUID();
      })
      .indexOf(id);
  }
}
