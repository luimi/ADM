import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Device } from '../../models/device.model';
import { WebsocketService } from '../../utils/websocket.service';

@Component({
  selector: 'device-check',
  templateUrl: './check.component.html',
  styleUrls: ['./check.component.scss']
})
export class CheckComponent implements OnInit {
  isChecking = false;
  @Output() onSuccess: EventEmitter<Device> = new EventEmitter()
  @Output() onFail: EventEmitter<any> = new EventEmitter()
  constructor(private websocketCtrl: WebsocketService) { }

  ngOnInit(): void {
  }
  async check(ip: string){
    if (this.isChecking) {
      return
    }
    this.isChecking = true;
    try {
      let device: any = await this.websocketCtrl.verify(ip);
      this.onSuccess.emit(device);
    } catch (e: any) {
      this.onFail.emit(e);
    }
    this.isChecking = false;
  }
}
