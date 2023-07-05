import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CustomerInfoFormService } from './customer-info-form.service';
import { CustomerInfoService } from '../service/customer-info.service';
import { ICustomerInfo } from '../customer-info.model';
import { IAddress } from 'app/entities/bookingservice/address/address.model';
import { AddressService } from 'app/entities/bookingservice/address/service/address.service';

import { CustomerInfoUpdateComponent } from './customer-info-update.component';

describe('CustomerInfo Management Update Component', () => {
  let comp: CustomerInfoUpdateComponent;
  let fixture: ComponentFixture<CustomerInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerInfoFormService: CustomerInfoFormService;
  let customerInfoService: CustomerInfoService;
  let addressService: AddressService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CustomerInfoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CustomerInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerInfoFormService = TestBed.inject(CustomerInfoFormService);
    customerInfoService = TestBed.inject(CustomerInfoService);
    addressService = TestBed.inject(AddressService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call address query and add missing value', () => {
      const customerInfo: ICustomerInfo = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const address: IAddress = { id: '3985a0e5-ca5c-4f1d-aa8f-05c774537d1f' };
      customerInfo.address = address;

      const addressCollection: IAddress[] = [{ id: '39e9e101-e9bc-428d-81b6-dcfa101cff89' }];
      jest.spyOn(addressService, 'query').mockReturnValue(of(new HttpResponse({ body: addressCollection })));
      const expectedCollection: IAddress[] = [address, ...addressCollection];
      jest.spyOn(addressService, 'addAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customerInfo });
      comp.ngOnInit();

      expect(addressService.query).toHaveBeenCalled();
      expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(addressCollection, address);
      expect(comp.addressesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const customerInfo: ICustomerInfo = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const address: IAddress = { id: '8250dedb-cb2d-4f97-ae48-0168a1919c37' };
      customerInfo.address = address;

      activatedRoute.data = of({ customerInfo });
      comp.ngOnInit();

      expect(comp.addressesCollection).toContain(address);
      expect(comp.customerInfo).toEqual(customerInfo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerInfo>>();
      const customerInfo = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(customerInfoFormService, 'getCustomerInfo').mockReturnValue(customerInfo);
      jest.spyOn(customerInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerInfo }));
      saveSubject.complete();

      // THEN
      expect(customerInfoFormService.getCustomerInfo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerInfoService.update).toHaveBeenCalledWith(expect.objectContaining(customerInfo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerInfo>>();
      const customerInfo = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(customerInfoFormService, 'getCustomerInfo').mockReturnValue({ id: null });
      jest.spyOn(customerInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerInfo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerInfo }));
      saveSubject.complete();

      // THEN
      expect(customerInfoFormService.getCustomerInfo).toHaveBeenCalled();
      expect(customerInfoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerInfo>>();
      const customerInfo = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(customerInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerInfoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAddress', () => {
      it('Should forward to addressService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(addressService, 'compareAddress');
        comp.compareAddress(entity, entity2);
        expect(addressService.compareAddress).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
