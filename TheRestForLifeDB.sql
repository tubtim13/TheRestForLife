CREATE TABLE Users(    userId int GENERATED ALWAYS AS IDENTITY not null primary key,
    fname varchar(150) not null,
    lname varchar(150) not null,
    email varchar(100) not null,
    password varchar(20) not null,
    address varchar(255) not null,
    tel     varchar(15) not null);


CREATE TABLE Orders (
    OrderId int GENERATED ALWAYS AS IDENTITY not null primary key,
    userId int not null,
    DateOrder timestamp,
    CONSTRAINT users_fk FOREIGN KEY (userId) REFERENCES Users (userId)
);

CREATE TABLE Types (
    typeId int GENERATED ALWAYS AS IDENTITY not null primary key,
    typetName varchar(100) not null
);
CREATE TABLE Products (
    productId int GENERATED ALWAYS AS IDENTITY not null primary key,
    productName varchar(100) not null,
    description varchar(255) not null,
    price int not null,
    benefits varchar(255) not null,
    species varchar(255) not null,
    addedDate timestamp not null,
    typeId int not null,
    CONSTRAINT Typess_fk FOREIGN KEY (typeId) REFERENCES Types (typeId)
);


CREATE TABLE OrderDetial (
    orderDeId int GENERATED ALWAYS AS IDENTITY not null primary key,
    amount int not null,
    productId int not null,
    OrderId int not null,
    CONSTRAINT Order_fk FOREIGN KEY (OrderId) REFERENCES Orders (OrderId),
    CONSTRAINT Products_fk FOREIGN KEY (productId) REFERENCES Products (productId)
);

------------------------------------------------------------------------------------
INSERT INTO TYPES (TYPETNAME)
VALUES ('ไม้ดอก'),('ไม้ประดับ'),('ไม้ยืนต้น');

INSERT INTO products (productName,DESCRIPTION,PRICE,BENEFITS,SPECIES,ADDEDDATE,TYPEID)
VALUES ('ชมพูพันธุ์ทิพย์','ชมพูพันธุ์ทิพย์เป็นไม้ยืนต้นผลัดใบ ขนาดกลางถึงใหญ่ เรือนยอดรูปไข่หรือทรงกลม แผ่กว้างเป็นชั้น ๆ 
เกิ่งเปราะหักง่าย ดอกมีสีชมพูอ่อน ชมพูสดถึงสีขาว กลางดอกสีเหลือง ออกเป็นช่อแบบช่อกระจุกที่ปลายกิ่ง มีดอกย่อยจำนวนมาก โคนกลีบดอกเชื่อมติดกันเป็นหลอดปลาย',1500,'ชมพูพันธุ์ทิพย์นิยมปลูกเป็นไม้ประดับเนื่องจากมีสีดอกที่สวยงาม
ใบต้มแก้เจ็บท้องหรือท้องเสีย หรือตำให้ละเอียดพอกใส่แผล ลำต้นใช้ทำฟืนและเยื่อใช้ทำกระดาษได้','no','2018-10-21 22:14:04.542',1),
('ปาล์มน้ำพุ','ลักษณะของกาบห่าง ทำให้ปาล์มน้ำพุโตเร็ว ไม่เป็นไม้ประดับที่ดี เนื่องจากเมื่ออายุประมาณ 5-10 ปี ลำต้นจะสูงชลูดไม่ตำกว่า 6 - 10 เมตร ไม่สวยงาม
 เจ้าของธุรกิจที่เพาะพันธุ์ปาล์มน้ำพุหากไม่รีบขายออกไปให้เร็วจะทำให้ปาล์มไม่เหลือฟอร์มไม้ประดับที่สวยงาม',600,'ปลูกประดับในสนาม หรือขอบแนวอาคาร
 โดยมีระยะห่างจากขอบอาคารไม่น้อยกว่า 5 เมตร','no','2018-10-21 22:14:04.542',2),
('สาวน้อยประแป้ง','จัดเป็นไม้ประดับต้น และใบที่นิยมปลูกในกระถางสำหรับประดับในอาคาร และนอกอาคาร เนื่องจาก แผ่นใบมีขนาดใหญ่ พื้นใบมีสีเขียว และประเป็นลายด่างด้วยสีขาวจนดูแปลกตา
 และสวยงาม',500,'1.สาวน้อยประแป้งนิยมปลูกเพื่อประดับต้น และใบเป็นหลัก เนื่องจาก แผ่นใบมีขนาดใหญ่ พื้นบีสีเขียว และมีลายประสีขาวทั่วใบ ซึ่งดูแปลกตา และสวยงาม
3.น้ำยางจากลำต้น ใบ และดอก ใช้เป็นยาพิษเบื่อสัตว์ แต่พึงระวัง หากคนกินอาจทำให้เสียชีวิตได้','Araceae','2018-10-21 22:14:04.542',2),
(' หูกระจง','เป็นไม้ที่มีทรงพุ่มสวยงามแตกกิ่งเป็นชั้น ๆ แต่ละชั้นห่างกันประมาณ 50-100 ซม. แม้หูกระจงเป็นไม้ผลัดใบแต่จะผลัดใบน้อยกว่าหูกวาง 
โดยปกติเป็นไม้ที่ชอบน้ำเมื่อนำไปปลูกในกระถางหรือลงดินแล้วรดน้ำให้ชุ่ม และสม่ำเสมอใบแทบจะไม่ร่วงเลย ',1500,'ปลูกเป็นไม้ประดับให้ร่มเงาแก่อาคารที่พักอาศัย ลาดจอดรถ หรือริมถนน
 มีพุ่มใบละเอียดแผ่เป็นชั้นสวยงาม ออกดอกช่วงกุมภาพันธ์ถึงเมษายน ควรปลูกโดยเว้นระยะห่าง 8 เมตร พุ่มจึงจะแผ่สวยงาม ต้นโตไว แต่ใบร่วงง่าย','Combretaceae',
'2018-10-21 22:14:04.542',3)
;

INSERT INTO USERS (FNAME,LNAME,EMAIL,PASSWORD,ADDRESS,TEL)
VALUES('Tubtim','Kuphkiao','tt@hot.com','123','51 Soi Thian Thale 25,
 Khwaeng Tha Kham, Khet Bang Khun Thian, Krung Thep Maha Nakhon 10150','0930744667l');