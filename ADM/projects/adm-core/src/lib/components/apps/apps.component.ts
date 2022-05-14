import { Component, OnInit } from '@angular/core';
import { DeviceComponent } from '../device/device.component';

@Component({
    selector: 'device-apps',
    templateUrl: './apps.component.html',
    styleUrls: ['./apps.component.scss']
})
export class AppsComponent extends DeviceComponent implements OnInit {
    filter: any[] = []
    filterInput = "";
    constructor() { 
        super();
    }

    ngOnInit(): void {
        this.filter = this.device.installedApps;
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
    search(input: any){
        this.filter = [];
        this.device.installedApps.forEach((app:any)=> {
            if(app.name.toLowerCase().includes(input.toLowerCase()) || app.packageName.toLowerCase().includes(input.toLowerCase())) this.filter.push(app);
        })
    }
}
