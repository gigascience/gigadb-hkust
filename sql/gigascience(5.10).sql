--PostgreSQL Maestro 11.8.0.2
------------------------------------------
--Host     : localhost
--Database : gigascience


-- Structure for table dataset (OID = 16472):
CREATE TABLE dataset (
    identifier varchar(15) NOT NULL,
    description varchar(3000) NOT NULL,
    publisher varchar(45) DEFAULT 'GigaScience'::character varying NOT NULL,
    publication_date date,
    modification_date date,
    title varchar(200) NOT NULL,
    release_date date,
    image_location varchar(45),
    ftp_site varchar(100) NOT NULL,
    dataset_size integer NOT NULL,
    submitter_email varchar(30) NOT NULL
) WITHOUT OIDS;
-- Structure for table image (OID = 16479):
CREATE TABLE image (
    image_location varchar(45) NOT NULL,
    image_tag varchar(50),
    image_url varchar(100),
    image_license varchar(250),
    image_photographer varchar(100),
    image_source varchar(200)
) WITHOUT OIDS;
-- Structure for table manuscript (OID = 16485):
CREATE TABLE manuscript (
    manuscript_doi varchar(50) NOT NULL,
    pmid integer
) WITHOUT OIDS;
-- Structure for table project (OID = 16488):
CREATE TABLE project (
    project_url varchar(45) NOT NULL,
    project_image_location varchar(100),
    project_name varchar(70) NOT NULL
) WITHOUT OIDS;
-- Structure for table external_link (OID = 16491):
CREATE TABLE external_link (
    link_url varchar(100) NOT NULL,
    link_type varchar(100) NOT NULL
) WITHOUT OIDS;
-- Structure for table submitter (OID = 16494):
CREATE TABLE submitter (
    submitter_handle varchar(30) NOT NULL,
    submitter_first_name varchar(100) NOT NULL,
    submitter_email varchar(30) NOT NULL,
    submitter_affiliation varchar(100) NOT NULL,
    submitter_password varchar(30) NOT NULL,
    submitter_last_name varchar(100)
) WITHOUT OIDS;
-- Structure for table species (OID = 16497):
CREATE TABLE species (
    tax_id integer NOT NULL,
    common_name varchar(30) NOT NULL,
    genbank_name varchar(40),
    scientific_name varchar(40)
) WITHOUT OIDS;
-- Structure for table sample (OID = 16500):
CREATE TABLE sample (
    sample_id varchar(50) NOT NULL,
    tax_id integer NOT NULL,
    sample_attributes varchar(200)
) WITHOUT OIDS;
-- Structure for table file (OID = 16503):
CREATE TABLE file (
    file_name varchar(100) NOT NULL,
    file_location varchar(200) NOT NULL,
    file_format varchar(10) NOT NULL,
    date_stamp date NOT NULL,
    file_description varchar(1000),
    file_extension varchar(30) NOT NULL,
    file_type varchar(100) NOT NULL,
    sample_id varchar(50),
    file_size bigint NOT NULL,
    identifier varchar(15) NOT NULL
) WITHOUT OIDS;
-- Structure for table datasettype (OID = 16509):
CREATE TABLE datasettype (
    identifier varchar(15) NOT NULL,
    dataset_type varchar(15) NOT NULL
) WITHOUT OIDS;
-- Structure for table dataset_manuscript (OID = 16512):
CREATE TABLE dataset_manuscript (
    manuscript_doi varchar(50) NOT NULL,
    identifier varchar(15) NOT NULL
) WITHOUT OIDS;
-- Structure for table dataset_sample (OID = 16515):
CREATE TABLE dataset_sample (
    identifier varchar(15) NOT NULL,
    sample_id varchar(50) NOT NULL
) WITHOUT OIDS;
-- Structure for table dataset_species (OID = 16518):
CREATE TABLE dataset_species (
    identifier varchar(15) NOT NULL,
    tax_id integer NOT NULL
) WITHOUT OIDS;
-- Structure for table dataset_project (OID = 16521):
CREATE TABLE dataset_project (
    identifier varchar(15) NOT NULL,
    project_url varchar(45) NOT NULL
) WITHOUT OIDS;
-- Structure for table author (OID = 16524):
CREATE TABLE author (
    author_name varchar(100) NOT NULL,
    identifier varchar(15) NOT NULL,
    orcid integer,
    rank integer NOT NULL
) WITHOUT OIDS;
-- Structure for table dataset_external_link (OID = 16527):
CREATE TABLE dataset_external_link (
    link_url varchar(100) NOT NULL,
    identifier varchar(15) NOT NULL
) WITHOUT OIDS;
-- Structure for table datasetext_acc_link (OID = 16530):
CREATE TABLE datasetext_acc_link (
    identifier varchar(15) NOT NULL,
    ext_acc_link varchar(100) NOT NULL
) WITHOUT OIDS;
-- Structure for table datasetext_acc_mirror (OID = 16533):
CREATE TABLE datasetext_acc_mirror (
    identifier varchar(15) NOT NULL,
    ext_acc_mirror varchar(100) NOT NULL
) WITHOUT OIDS;
-- Structure for table reserved_dataset (OID = 16536):
CREATE TABLE reserved_dataset (
    identifier varchar(15) NOT NULL,
    notes varchar(100),
    reserved_date date NOT NULL
) WITHOUT OIDS;
-- Structure for table excelfile (OID = 16539):
CREATE TABLE excelfile (
    identifier varchar(15) NOT NULL,
    excelfile_name varchar(50) NOT NULL,
    md5 varchar(32) NOT NULL
) WITHOUT OIDS;
-- Definition for index manuscript_pkey (OID = 16542):
ALTER TABLE ONLY manuscript
    ADD CONSTRAINT manuscript_pkey PRIMARY KEY (manuscript_doi);
-- Definition for index file_pkey (OID = 16544):
ALTER TABLE ONLY file
    ADD CONSTRAINT file_pkey PRIMARY KEY (file_location);
-- Definition for index sample_pkey (OID = 16546):
ALTER TABLE ONLY sample
    ADD CONSTRAINT sample_pkey PRIMARY KEY (sample_id);
-- Definition for index species_pkey (OID = 16548):
ALTER TABLE ONLY species
    ADD CONSTRAINT species_pkey PRIMARY KEY (tax_id);
-- Definition for index dataset_pkey (OID = 16550):
ALTER TABLE ONLY dataset
    ADD CONSTRAINT dataset_pkey PRIMARY KEY (identifier);
-- Definition for index datasetType_pkey (OID = 16552):
ALTER TABLE ONLY datasettype
    ADD CONSTRAINT "datasetType_pkey" PRIMARY KEY (identifier, dataset_type);
-- Definition for index foreign_key01 (OID = 16554):
ALTER TABLE ONLY datasettype
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index dataset_manuscript_pkey (OID = 16559):
ALTER TABLE ONLY dataset_manuscript
    ADD CONSTRAINT dataset_manuscript_pkey PRIMARY KEY (manuscript_doi, identifier);
-- Definition for index foreign_key01 (OID = 16561):
ALTER TABLE ONLY dataset_manuscript
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (manuscript_doi) REFERENCES manuscript(manuscript_doi);
-- Definition for index foreign_key02 (OID = 16566):
ALTER TABLE ONLY dataset_manuscript
    ADD CONSTRAINT foreign_key02 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index dataset_sample_pkey (OID = 16571):
ALTER TABLE ONLY dataset_sample
    ADD CONSTRAINT dataset_sample_pkey PRIMARY KEY (identifier, sample_id);
-- Definition for index foreign_key01 (OID = 16573):
ALTER TABLE ONLY dataset_sample
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index foreign_key02 (OID = 16578):
ALTER TABLE ONLY dataset_sample
    ADD CONSTRAINT foreign_key02 FOREIGN KEY (sample_id) REFERENCES sample(sample_id) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index foreign_key01 (OID = 16583):
ALTER TABLE ONLY file
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (sample_id) REFERENCES sample(sample_id) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index dataset_species_pkey (OID = 16588):
ALTER TABLE ONLY dataset_species
    ADD CONSTRAINT dataset_species_pkey PRIMARY KEY (identifier, tax_id);
-- Definition for index foreign_key01 (OID = 16590):
ALTER TABLE ONLY dataset_species
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index foreign_key02 (OID = 16595):
ALTER TABLE ONLY dataset_species
    ADD CONSTRAINT foreign_key02 FOREIGN KEY (tax_id) REFERENCES species(tax_id) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index foreign_key01 (OID = 16600):
ALTER TABLE ONLY dataset_project
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index foreign_key01 (OID = 16605):
ALTER TABLE ONLY author
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index foreign_key02 (OID = 16610):
ALTER TABLE ONLY dataset_external_link
    ADD CONSTRAINT foreign_key02 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index Foreign_key01 (OID = 16615):
ALTER TABLE ONLY datasetext_acc_link
    ADD CONSTRAINT "Foreign_key01" FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index Foreign_key01 (OID = 16620):
ALTER TABLE ONLY datasetext_acc_mirror
    ADD CONSTRAINT "Foreign_key01" FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index Foreign_key01 (OID = 16625):
ALTER TABLE ONLY sample
    ADD CONSTRAINT "Foreign_key01" FOREIGN KEY (tax_id) REFERENCES species(tax_id) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index datasetExt_acc_mirror_pkey (OID = 16630):
ALTER TABLE ONLY datasetext_acc_mirror
    ADD CONSTRAINT "datasetExt_acc_mirror_pkey" PRIMARY KEY (identifier, ext_acc_mirror);
-- Definition for index ext_acc_link_pkey (OID = 16632):
ALTER TABLE ONLY datasetext_acc_link
    ADD CONSTRAINT ext_acc_link_pkey PRIMARY KEY (identifier, ext_acc_link);
-- Definition for index dataset_external_link_pkey (OID = 16634):
ALTER TABLE ONLY dataset_external_link
    ADD CONSTRAINT dataset_external_link_pkey PRIMARY KEY (link_url, identifier);
-- Definition for index externel_link_pkey (OID = 16636):
ALTER TABLE ONLY external_link
    ADD CONSTRAINT externel_link_pkey PRIMARY KEY (link_url);
-- Definition for index foreign_key01 (OID = 16638):
ALTER TABLE ONLY dataset_external_link
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (link_url) REFERENCES external_link(link_url) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index image_pkey (OID = 16643):
ALTER TABLE ONLY image
    ADD CONSTRAINT image_pkey PRIMARY KEY (image_location);
-- Definition for index submitter_pkey (OID = 16645):
ALTER TABLE ONLY submitter
    ADD CONSTRAINT submitter_pkey PRIMARY KEY (submitter_email);
-- Definition for index Foreign_key01 (OID = 16647):
ALTER TABLE ONLY dataset
    ADD CONSTRAINT "Foreign_key01" FOREIGN KEY (submitter_email) REFERENCES submitter(submitter_email) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index Foreign_key02 (OID = 16652):
ALTER TABLE ONLY dataset
    ADD CONSTRAINT "Foreign_key02" FOREIGN KEY (image_location) REFERENCES image(image_location) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index author_pkey (OID = 16657):
ALTER TABLE ONLY author
    ADD CONSTRAINT author_pkey PRIMARY KEY (author_name, identifier, rank);
-- Definition for index Foreign_key01 (OID = 16659):
ALTER TABLE ONLY file
    ADD CONSTRAINT "Foreign_key01" FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index dataset_project_pkey (OID = 16664):
ALTER TABLE ONLY dataset_project
    ADD CONSTRAINT dataset_project_pkey PRIMARY KEY (identifier, project_url);
-- Definition for index project_pkey (OID = 16666):
ALTER TABLE ONLY project
    ADD CONSTRAINT project_pkey PRIMARY KEY (project_url);
-- Definition for index Foreign_key01 (OID = 16668):
ALTER TABLE ONLY dataset_project
    ADD CONSTRAINT "Foreign_key01" FOREIGN KEY (project_url) REFERENCES project(project_url) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index reserved_doi_pkey (OID = 16673):
ALTER TABLE ONLY reserved_dataset
    ADD CONSTRAINT reserved_doi_pkey PRIMARY KEY (identifier);
-- Definition for index excelfile_pkey (OID = 16675):
ALTER TABLE ONLY excelfile
    ADD CONSTRAINT excelfile_pkey PRIMARY KEY (identifier);
