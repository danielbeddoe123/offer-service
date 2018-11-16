insert into merchant (merchant_Id) VALUES ('2C8BE77DC75140CCA39449659CED9621')
insert into merchandise (merchandise_Id, merchandise_Type, merchant_merchant_Id) VALUES ('F1CAD633863D443CA494C993FDEE8D4F', 'PRODUCT', '2C8BE77DC75140CCA39449659CED9621')
insert into offer (offer_Id, description, merchandise_merchandise_Id, currency_Code, price, active, expiry_Date) VALUES ('B32C7B9883F44733A751AEB069C2320C', 'Product 2', 'F1CAD633863D443CA494C993FDEE8D4F', 'GBP', 30.00, true, to_date('01/03/2019', 'dd/MM/yyyy'))
