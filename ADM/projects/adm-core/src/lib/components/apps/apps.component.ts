import { Component, OnInit } from '@angular/core';
import { DeviceComponent } from '../device/device.component';

@Component({
    selector: 'device-apps',
    templateUrl: './apps.component.html',
    styleUrls: ['./apps.component.scss']
})
export class AppsComponent extends DeviceComponent implements OnInit {

    constructor() { 
        super();
    }

    ngOnInit(): void {
    }
    launch(packageName: String){
        this.send({type:'startapp', value1: packageName})
    }
    clear(packageName: String){
        this.send({type:'clearapp', value1: packageName})
    }
    remove(packageName: String){
        this.send({type:'removeapp', value1: packageName})
    }
}
