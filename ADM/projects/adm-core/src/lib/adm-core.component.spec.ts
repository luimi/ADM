import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdmCoreComponent } from './adm-core.component';

describe('AdmCoreComponent', () => {
  let component: AdmCoreComponent;
  let fixture: ComponentFixture<AdmCoreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdmCoreComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdmCoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
