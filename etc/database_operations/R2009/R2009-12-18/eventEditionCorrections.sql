update EVENT_CONFERENCE_ARTICLES_ASSOCIATION set OID_EVENT_EDITION = NULL where OID_EVENT_EDITION not in (select OID from EVENT_EDITION);
alter table DELETE_FILE_REQUEST modify REQUESTOR_IST_USERNAME varchar(255) NULL;