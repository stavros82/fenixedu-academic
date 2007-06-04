CREATE TABLE EXTERNAL_ENROLMENT_NOT_NEED_TO_ENROLL_IN_CURRICULAR_COURSE (
  `KEY_EXTERNAL_ENROLMENT` INTEGER(11) NOT NULL,
  `KEY_NOT_NEED_TO_ENROLL_IN_CURRICULAR_COURSE` INTEGER(11) NOT NULL,
   PRIMARY KEY(`KEY_EXTERNAL_ENROLMENT`,`KEY_NOT_NEED_TO_ENROLL_IN_CURRICULAR_COURSE`),
   KEY KEY_EXTERNAL_ENROLMENT (`KEY_EXTERNAL_ENROLMENT`),
   KEY KEY_NOT_NEED_TO_ENROLL_IN_CURRICULAR_COURSE (`KEY_NOT_NEED_TO_ENROLL_IN_CURRICULAR_COURSE`)
) type=InnoDB;
