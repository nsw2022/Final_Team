
CREATE TABLE company (
    cno int  NOT NULL AUTO_INCREMENT primary key,
	cid varchar(20) ,
    cpw VARCHAR(20),
    cname VARCHAR(30),
    cbuild VARCHAR(30),
    ccategory varchar(60),
    cstaff int,
    csales int,
    ccall varchar(30),
    cceo varchar(20),
    caddress varchar(100),
    ccompanyFile  VARCHAR(100),
    ccontent varchar(300),
    ctype Int,
    cdate date,
    cbcnt int
);

INSERT INTO company (cid, cpw, cname, cbuild, ccategory, cstaff, csales, ccall, cceo, caddress, ccompanyFile, ccontent, ctype, cdate, cbcnt)
VALUES
    ('company1', 'password1', 'ABC Company', 'Building A', 'Technology', 100, 1000000, '123-456-7890', 'John Doe', '123 Main St', 'file1.txt', 'This is a technology company.', 1, '2023-09-04', 5),
    ('company2', 'password2', 'XYZ Corporation', 'Tower 1', 'Finance', 50, 500000, '987-654-3210', 'Jane Smith', '456 Oak St', 'file2.txt', 'This is a finance company.', 2, '2023-09-04', 3),
    ('company3', 'password3', 'PQR Ltd.', 'Office Park', 'Manufacturing', 200, 2000000, '555-123-4567', 'Bob Johnson', '789 Elm St', 'file3.txt', 'This is a manufacturing company.', 1, '2023-09-04', 7),
    ('company4', 'password4', 'LMN Industries', 'Complex B', 'Engineering', 75, 750000, '111-222-3333', 'Alice Lee', '101 Engineering Ave', 'file4.txt', 'This is an engineering company.', 2, '2023-09-04', 2),
    ('company5', 'password5', 'RetailMart', 'Mall Plaza', 'Retail', 30, 300000, '444-555-6666', 'Eve Brown', 'Shop 5, Mall Street', 'file5.txt', 'This is a retail company.', 1, '2023-09-04', 6);

select * from company;
drop table company;