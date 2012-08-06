--PostgreSQL Maestro 11.8.0.2
------------------------------------------
--Host     : localhost
--Database : gigascience


-- Structure for table dataset (OID = 16394):
CREATE TABLE dataset (
    identifier varchar(15) NOT NULL,
    description varchar(3000) NOT NULL,
    publisher varchar(45) DEFAULT 'GigaScience'::character varying NOT NULL,
    publication_date date NOT NULL,
    modification_date date,
    title varchar(200) NOT NULL,
    release_date date NOT NULL,
    image_url varchar(45),
    ftp_site varchar(100) NOT NULL,
    dataset_size integer NOT NULL,
    submitter_email varchar(30) NOT NULL
) WITHOUT OIDS;
-- Structure for table image (OID = 16404):
CREATE TABLE image (
    image_location varchar(45),
    image_tag varchar(50) NOT NULL,
    image_url varchar(100) NOT NULL,
    image_license varchar(100) NOT NULL,
    image_photographer varchar(100) NOT NULL,
    image_source varchar(100) NOT NULL
) WITHOUT OIDS;
-- Structure for table manuscript (OID = 16407):
CREATE TABLE manuscript (
    manuscript_doi varchar(50) NOT NULL,
    pmid integer
) WITHOUT OIDS;
-- Structure for table project (OID = 16410):
CREATE TABLE project (
    project_name varchar(50) NOT NULL,
    project_url varchar(45) NOT NULL,
    project_image_location varchar(100),
    project_image_tag varchar(50)
) WITHOUT OIDS;
-- Structure for table external_link (OID = 16413):
CREATE TABLE external_link (
    link_url varchar(100) NOT NULL,
    link_type varchar(100) NOT NULL
) WITHOUT OIDS;
-- Structure for table submitter (OID = 16419):
CREATE TABLE submitter (
    submitter_handle varchar(30) NOT NULL,
    submitter_first_name varchar(100) NOT NULL,
    submitter_email varchar(30) NOT NULL,
    submitter_affiliation varchar(100) NOT NULL,
    submitter_password varchar(30) NOT NULL,
    submitter_last_name varchar(100)
) WITHOUT OIDS;
-- Structure for table species (OID = 16422):
CREATE TABLE species (
    tax_id integer NOT NULL,
    common_name varchar(30) NOT NULL
) WITHOUT OIDS;
-- Structure for table sample (OID = 16428):
CREATE TABLE sample (
    sample_id varchar(50) NOT NULL,
    tax_id integer NOT NULL
) WITHOUT OIDS;
-- Structure for table file (OID = 16431):
CREATE TABLE file (
    file_name varchar(100) NOT NULL,
    file_location varchar(200) NOT NULL,
    file_type varchar(10) NOT NULL,
    file_size varchar(20) NOT NULL,
    date_stamp date NOT NULL,
    file_text varchar(1000),
    file_extension varchar(10) NOT NULL,
    file_description varchar(100) NOT NULL,
    sample_id varchar(50)
) WITHOUT OIDS;
-- Structure for table datasettype (OID = 16452):
CREATE TABLE datasettype (
    identifier varchar(15) NOT NULL,
    dataset_type varchar(15) NOT NULL
) WITHOUT OIDS;
-- Structure for table dataset_manuscript (OID = 16455):
CREATE TABLE dataset_manuscript (
    manuscript_doi varchar(50) NOT NULL,
    identifier varchar(15) NOT NULL
) WITHOUT OIDS;
-- Structure for table dataset_file (OID = 16464):
CREATE TABLE dataset_file (
    identifier varchar(15) NOT NULL,
    file_location varchar(200) NOT NULL
) WITHOUT OIDS;
-- Structure for table dataset_sample (OID = 16467):
CREATE TABLE dataset_sample (
    identifier varchar(15) NOT NULL,
    sample_id varchar(50) NOT NULL
) WITHOUT OIDS;
-- Structure for table dataset_species (OID = 16470):
CREATE TABLE dataset_species (
    identifier varchar(15) NOT NULL,
    tax_id integer NOT NULL
) WITHOUT OIDS;
-- Structure for table dataset_project (OID = 16476):
CREATE TABLE dataset_project (
    identifier varchar(15) NOT NULL,
    project_name varchar(50) NOT NULL
) WITHOUT OIDS;
-- Structure for table author (OID = 16479):
CREATE TABLE author (
    author_name varchar(100) NOT NULL,
    identifier varchar(15) NOT NULL,
    orcid integer,
    rank integer NOT NULL
) WITHOUT OIDS;
-- Structure for table dataset_external_link (OID = 16783):
CREATE TABLE dataset_external_link (
    link_url varchar(100) NOT NULL,
    identifier varchar(15) NOT NULL
) WITHOUT OIDS;
-- Structure for table datasetext_acc_link (OID = 16839):
CREATE TABLE datasetext_acc_link (
    identifier varchar(15) NOT NULL,
    ext_acc_link varchar(100) NOT NULL
) WITHOUT OIDS;
-- Structure for table datasetext_acc_mirror (OID = 16849):
CREATE TABLE datasetext_acc_mirror (
    identifier varchar(15) NOT NULL,
    ext_acc_mirror varchar(100) NOT NULL
) WITHOUT OIDS;
-- Definition for index manuscript_pkey (OID = 16487):
ALTER TABLE ONLY manuscript
    ADD CONSTRAINT manuscript_pkey PRIMARY KEY (manuscript_doi);
-- Definition for index project_pkey (OID = 16489):
ALTER TABLE ONLY project
    ADD CONSTRAINT project_pkey PRIMARY KEY (project_name);
-- Definition for index file_pkey (OID = 16512):
ALTER TABLE ONLY file
    ADD CONSTRAINT file_pkey PRIMARY KEY (file_location);
-- Definition for index sample_pkey (OID = 16514):
ALTER TABLE ONLY sample
    ADD CONSTRAINT sample_pkey PRIMARY KEY (sample_id);
-- Definition for index species_pkey (OID = 16516):
ALTER TABLE ONLY species
    ADD CONSTRAINT species_pkey PRIMARY KEY (tax_id);
-- Definition for index dataset_pkey (OID = 16518):
ALTER TABLE ONLY dataset
    ADD CONSTRAINT dataset_pkey PRIMARY KEY (identifier);
-- Definition for index datasetType_pkey (OID = 16545):
ALTER TABLE ONLY datasettype
    ADD CONSTRAINT "datasetType_pkey" PRIMARY KEY (identifier, dataset_type);
-- Definition for index foreign_key01 (OID = 16547):
ALTER TABLE ONLY datasettype
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index dataset_manuscript_pkey (OID = 16552):
ALTER TABLE ONLY dataset_manuscript
    ADD CONSTRAINT dataset_manuscript_pkey PRIMARY KEY (manuscript_doi, identifier);
-- Definition for index foreign_key01 (OID = 16554):
ALTER TABLE ONLY dataset_manuscript
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (manuscript_doi) REFERENCES manuscript(manuscript_doi);
-- Definition for index foreign_key02 (OID = 16559):
ALTER TABLE ONLY dataset_manuscript
    ADD CONSTRAINT foreign_key02 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index dataset_file_pkey (OID = 16596):
ALTER TABLE ONLY dataset_file
    ADD CONSTRAINT dataset_file_pkey PRIMARY KEY (identifier, file_location);
-- Definition for index foreign_key01 (OID = 16598):
ALTER TABLE ONLY dataset_file
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index foreign_key02 (OID = 16603):
ALTER TABLE ONLY dataset_file
    ADD CONSTRAINT foreign_key02 FOREIGN KEY (file_location) REFERENCES file(file_location) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index dataset_sample_pkey (OID = 16608):
ALTER TABLE ONLY dataset_sample
    ADD CONSTRAINT dataset_sample_pkey PRIMARY KEY (identifier, sample_id);
-- Definition for index foreign_key01 (OID = 16610):
ALTER TABLE ONLY dataset_sample
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index foreign_key02 (OID = 16615):
ALTER TABLE ONLY dataset_sample
    ADD CONSTRAINT foreign_key02 FOREIGN KEY (sample_id) REFERENCES sample(sample_id) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index foreign_key01 (OID = 16620):
ALTER TABLE ONLY file
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (sample_id) REFERENCES sample(sample_id) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index dataset_species_pkey (OID = 16625):
ALTER TABLE ONLY dataset_species
    ADD CONSTRAINT dataset_species_pkey PRIMARY KEY (identifier, tax_id);
-- Definition for index foreign_key01 (OID = 16627):
ALTER TABLE ONLY dataset_species
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index foreign_key02 (OID = 16632):
ALTER TABLE ONLY dataset_species
    ADD CONSTRAINT foreign_key02 FOREIGN KEY (tax_id) REFERENCES species(tax_id) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index dataset_project_pkey (OID = 16649):
ALTER TABLE ONLY dataset_project
    ADD CONSTRAINT dataset_project_pkey PRIMARY KEY (identifier, project_name);
-- Definition for index foreign_key01 (OID = 16651):
ALTER TABLE ONLY dataset_project
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index foreign_key02 (OID = 16656):
ALTER TABLE ONLY dataset_project
    ADD CONSTRAINT foreign_key02 FOREIGN KEY (project_name) REFERENCES project(project_name) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index author_pkey (OID = 16661):
ALTER TABLE ONLY author
    ADD CONSTRAINT author_pkey PRIMARY KEY (author_name, identifier);
-- Definition for index foreign_key01 (OID = 16663):
ALTER TABLE ONLY author
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index foreign_key02 (OID = 16803):
ALTER TABLE ONLY dataset_external_link
    ADD CONSTRAINT foreign_key02 FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index Foreign_key01 (OID = 16844):
ALTER TABLE ONLY datasetext_acc_link
    ADD CONSTRAINT "Foreign_key01" FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index Foreign_key01 (OID = 16854):
ALTER TABLE ONLY datasetext_acc_mirror
    ADD CONSTRAINT "Foreign_key01" FOREIGN KEY (identifier) REFERENCES dataset(identifier) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index Foreign_key01 (OID = 16863):
ALTER TABLE ONLY sample
    ADD CONSTRAINT "Foreign_key01" FOREIGN KEY (tax_id) REFERENCES species(tax_id) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index datasetExt_acc_mirror_pkey (OID = 16868):
ALTER TABLE ONLY datasetext_acc_mirror
    ADD CONSTRAINT "datasetExt_acc_mirror_pkey" PRIMARY KEY (identifier, ext_acc_mirror);
-- Definition for index ext_acc_link_pkey (OID = 16874):
ALTER TABLE ONLY datasetext_acc_link
    ADD CONSTRAINT ext_acc_link_pkey PRIMARY KEY (identifier, ext_acc_link);
-- Definition for index dataset_external_link_pkey (OID = 16880):
ALTER TABLE ONLY dataset_external_link
    ADD CONSTRAINT dataset_external_link_pkey PRIMARY KEY (link_url, identifier);
-- Definition for index externel_link_pkey (OID = 16891):
ALTER TABLE ONLY external_link
    ADD CONSTRAINT externel_link_pkey PRIMARY KEY (link_url);
-- Definition for index foreign_key01 (OID = 16893):
ALTER TABLE ONLY dataset_external_link
    ADD CONSTRAINT foreign_key01 FOREIGN KEY (link_url) REFERENCES external_link(link_url) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index image_pkey (OID = 16902):
ALTER TABLE ONLY image
    ADD CONSTRAINT image_pkey PRIMARY KEY (image_url);
-- Definition for index Foreign_key02 (OID = 16914):
ALTER TABLE ONLY dataset
    ADD CONSTRAINT "Foreign_key02" FOREIGN KEY (image_url) REFERENCES image(image_url) ON UPDATE CASCADE ON DELETE CASCADE;
-- Definition for index submitter_pkey (OID = 16919):
ALTER TABLE ONLY submitter
    ADD CONSTRAINT submitter_pkey PRIMARY KEY (submitter_email);
-- Definition for index Foreign_key01 (OID = 16921):
ALTER TABLE ONLY dataset
    ADD CONSTRAINT "Foreign_key01" FOREIGN KEY (submitter_email) REFERENCES submitter(submitter_email) ON UPDATE CASCADE ON DELETE CASCADE;
