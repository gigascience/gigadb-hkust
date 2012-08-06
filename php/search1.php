<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
 <meta name="keywords" content="" />
 <meta name="description" content="" />
 <meta http-equiv="content-type" content="text/html; charset=utf-8" />
 <title>why not?</title> 
 </head><body>
<?php
//phpinfo();

//echo @$_GET['title'];
//$dbconn   =   pg_connect( "host=localhost   dbname=marend   user=postgres   password=4426asr ") or   die( 'Could   not   connect:   '   .   pg_last_error()); 
$dbconn   =   pg_connect( "host=192.168.171.46   dbname=GigaDB   user=jesse   password=1234") or   die( 'Could   not   connect:   '   .   pg_last_error()); 
//echo @$_GET['cont1'];
$i=0;
$query=array();
while($i<10)
{
if(@$_GET['item'.$i]==1){
$query[$i]   =   "SELECT   title, identifier , ftp_site, publication_date,modification_date  FROM  dataset  where identifier in (select identifier from author where UPPER(author_name) like UPPER('%".@$_GET['cont'.$i]."%') union select identifier from datasettype where UPPER(dataset_type) like UPPER('".@$_GET['cont'.$i]."') 
union select identifier from datasetext_acc_link where ext_acc_link ='%".@$_GET['cont'.$i]."%' union select identifier from dataset_external_link where link_url ='%".@$_GET['cont'.$i]."%' 
union select distinct identifier  from file  where file_description like '%".@$_GET['cont'.$i]."%'  or file_extension like '%".@$_GET['cont'.$i]."%' or  UPPER(file_name) like UPPER('%".@$_GET['cont'.$i]."%')  or  UPPER(file_type) like UPPER('%".@$_GET['cont'.$i]."%') or UPPER(sample_id) like UPPER('%".@$_GET['cont'.$i]."%') union select identifier from dataset_manuscript  where manuscript_doi like '%".@$_GET['cont'.$i]."%'  
union select  identifier from dataset_project where project_url in (select project_url from project where UPPER(project_name) like UPPER('%".@$_GET['cont'.$i]."%'))  union select identifier from dataset_external_link, external_link where  dataset_external_link.link_url=external_link.link_url and link_type ='%".@$_GET['cont'.$i]."%' ) or  UPPER(description) like UPPER('%".@$_GET['cont'.$i]."%') or ftp_site like '%".@$_GET['cont'.$i]."%' or  identifier like '%".@$_GET['cont'.$i]."%' or publisher like  '%".@$_GET['cont'.$i]."%' or  submitter_email in (select submitter_email from submitter where submitter_handle like '%".@$_GET['cont'.$i]."%' or UPPER(submitter_affiliation) like UPPER('%".@$_GET['cont'.$i]."%'))";
}
if(@$_GET['item'.$i]==2){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM  dataset  where identifier in (select identifier from author where UPPER(author_name) like UPPER('%".@$_GET['cont'.$i]."%'))";
}
if(@$_GET['item'.$i]==3){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM  dataset  where identifier in (select identifier from datasettype where UPPER(dataset_type) like UPPER('".@$_GET['cont'.$i]."')) ";
}
if(@$_GET['item'.$i]==4){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date ,upload_status   FROM  dataset  where  identifier in (select identifier from species, dataset_species where dataset_species.tax_id=species.tax_id and UPPER(genbank_name) like UPPER('%".$_GET['cont'.$i]."%'))";
}
if(@$_GET['item'.$i]==5){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM   dataset where UPPER(description) like UPPER('%".@$_GET['cont'.$i]."%') ";
}

if(@$_GET['item'.$i]==6){
$query[$i]   =   "SELECT   title, identifier,ftp_site, publication_date,modification_date,upload_status    FROM  dataset  where identifier in (select identifier from datasetext_acc_link where  ext_acc_link like '%".@$_GET['cont'.$i]."%'  union select identifier from datasetext_acc_mirror where   ext_acc_mirror like '%".@$_GET['cont'.$i]."%')";
}
if(@$_GET['item'.$i]==7){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM  dataset  where  identifier in (select identifier from species, dataset_species where dataset_species.tax_id=species.tax_id and UPPER( scientific_name) like UPPER('%".$_GET['cont'.$i]."%'))";
}
if(@$_GET['item'.$i]==8){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date ,upload_status   FROM  dataset  where identifier in (select identifier from dataset_external_link  where  link_url like '%".@$_GET['cont'.$i]."%')";
}
if(@$_GET['item'.$i]==9){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date ,upload_status   FROM  dataset where identifier in (select distinct identifier from file   where  UPPER(file_description) like UPPER('%".@$_GET['cont'.$i]."%'))";
}
if(@$_GET['item'.$i]==10){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM  dataset where identifier in (select identifier from file   where file_extension like '%".@$_GET['cont'.$i]."%')";
}
if(@$_GET['item'.$i]==11){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM  dataset where identifier in (select identifier from file   where UPPER(file_name) like UPPER('%".@$_GET['cont'.$i]."%'))";
}
if(@$_GET['item'.$i]==12){
$size=doubleval($_GET['cont'.$i]);
if(stripos($_GET['cont'.$i],"k"))
	{$size=doubleval(substr(@$_GET['cont'.$i],0,stripos(@$_GET['cont'.$i],'k')))*1024;
//echo $size;
}
if(stripos($_GET['cont'.$i],"m"))
	{$size=doubleval(substr(@$_GET['cont'.$i],0,stripos(@$_GET['cont'.$i],'m')))*1024*1024;
//echo $size;
}
if(stripos($_GET['cont'.$i],"g"))
	{$size=doubleval(substr(@$_GET['cont'.$i],0,stripos(@$_GET['cont'.$i],'g')))*1024*1024*1024;}
if(stripos($_GET['cont'.$i],"t"))
	{$size=doubleval(substr(@$_GET['cont'.$i],0,stripos(@$_GET['cont'.$i],'t')))*1024*1024*1024*1024;
//echo $size;
}
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM  dataset  where identifier in (select identifier from file  where file_size < ".strval($size).")";}
if(@$_GET['item'.$i]==13){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM  dataset where identifier in (select identifier from file   where UPPER(file_format) like UPPER('%".@$_GET['cont'.$i]."%'))";
}
if(@$_GET['item'.$i]==14){
$query[$i]   =   "SELECT   title, identifier,ftp_site, publication_date,modification_date,upload_status     FROM  dataset  where identifier in (select identifier from file  where  UPPER(file_type) like UPPER('".@$_GET['cont'.$i]."'))";
}
if(@$_GET['item'.$i]==15){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM  dataset where   ftp_site  like '%".@$_GET['cont'.$i]."%'";
}
if(@$_GET['item'.$i]==16){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM   dataset where identifier like '%".@$_GET['cont'.$i]."%'";
}
if(@$_GET['item'.$i]==17){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM   dataset where identifier in (select identifier from dataset_manuscript  where manuscript_doi like '%".@$_GET['cont'.$i]."%')";
}
if(@$_GET['item'.$i]==18){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date ,upload_status   FROM   dataset where modification_date < '".@$_GET['cont'.$i]."'";}
if(@$_GET['item'.$i]==19){
$query[$i]   =    "SELECT   title, identifier ,ftp_site, publication_date,modification_date ,upload_status   FROM  dataset  where identifier in (select  identifier from author where orcid = ".@$_GET['cont'.$i].")";
}
if(@$_GET['item'.$i]==20){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM   dataset where identifier in (select identifier from dataset_manuscript, manuscript where dataset_manuscript.manuscript_doi= manuscript.manuscript_doi and pmid=".@$_GET['cont'.$i].")";
}
if(@$_GET['item'.$i]==21){
$query[$i]   =    "SELECT   title, identifier,ftp_site, publication_date,modification_date ,upload_status    FROM  dataset  where identifier in (select  identifier from dataset_project where project_url in (select project_url from project where UPPER(project_name) like UPPER('%".@$_GET['cont'.$i]."%')))";
}

if(@$_GET['item'.$i]==22){
$query[$i]   =   "SELECT   title, identifier,ftp_site, publication_date,modification_date,upload_status     FROM   dataset where publication_date< '".@$_GET['cont'.$i]."'";}
if(@$_GET['item'.$i]==23){
$query[$i]   =   "SELECT   title, identifier,ftp_site, publication_date,modification_date ,upload_status    FROM   dataset where UPPER(publisher) like  UPPER('%".@$_GET['cont'.$i]."%')";}
if(@$_GET['item'.$i]==24){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date ,upload_status   FROM   dataset where release_date< '".@$_GET['cont'.$i]."'";}
if(@$_GET['item'.$i]==25){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date ,upload_status   FROM  dataset  where identifier in (select identifier  from dataset_sample where UPPER(sample_id) like UPPER('%".@$_GET['cont'.$i]."%'))";}
if(@$_GET['item'.$i]==26){
$query[$i]   =   "SELECT  title, identifier,ftp_site, publication_date,modification_date,upload_status    FROM   dataset where submitter_email in (select submitter_email from submitter where UPPER(submitter_affiliation) like UPPER('%".@$_GET['cont'.$i]."%'))";
}

if(@$_GET['item'.$i]==27){
$query[$i]   =   "SELECT  title, identifier,ftp_site, publication_date,modification_date,upload_status    FROM   dataset where submitter_email in (select submitter_email from submitter where submitter_handle like '%".@$_GET['cont'.$i]."%')";
}
if(@$_GET['item'.$i]==28){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM  dataset  where identifier in (select identifier from dataset_species where tax_id=".@$_GET['cont'.$i].")";
}

if(@$_GET['item'.$i]==29){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM   dataset where UPPER(title)  like UPPER('%".@$_GET['cont'.$i]."%') ";
}
if(@$_GET['item'.$i]==30){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM  dataset  where identifier in (select identifier from dataset_external_link, external_link where  dataset_external_link.link_url=external_link.link_url and link_type ='".@$_GET['cont'.$i]."')";
}
if(@$_GET['item'.$i]==31){
$query[$i]   =   "SELECT  title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM   dataset where submitter_handle in (select submitter_handle from submitter where submitter_name like '%".@$_GET['cont'.$i]."%')";
}
if(@$_GET['item'.$i]==32){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date,upload_status    FROM   dataset where image_location like '".@$_GET['cont'.$i]."' ";
}

if(@$_GET['item'.$i]==33){
$query[$i]   =   "SELECT   title, identifier,ftp_site, publication_date,modification_date,upload_status     FROM   dataset where image_location in (select image_location from image where image_type like '".@$_GET['cont'.$i]."' )";
}
if(@$_GET['item'.$i]==34){
$query[$i]   =   "SELECT   title, identifier,ftp_site, publication_date,modification_date ,upload_status    FROM  dataset  where date_stamp > '".@$_GET['cont'.$i]."'";
}
if(@$_GET['item'.$i]==35){
$query[$i]   =   "SELECT   title, identifier,ftp_site, publication_date,modification_date,upload_status     FROM   dataset where modification_date > '".@$_GET['cont'.$i]."'";}

if(@$_GET['item'.$i]==36){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date ,upload_status   FROM   dataset where publication_date> '".@$_GET['cont'.$i]."'";}
if(@$_GET['item'.$i]==37){
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date ,upload_status   FROM   dataset where release_date> '".@$_GET['cont'.$i]."'";}
if(@$_GET['item'.$i]==38){
	$size=doubleval($_GET['cont'.$i]);
if(stripos($_GET['cont'.$i],"k"))
	{$size=doubleval(substr(@$_GET['cont'.$i],0,stripos(@$_GET['cont'.$i],'k')))*1024;
//echo $size;
}
if(stripos($_GET['cont'.$i],"m"))
	{$size=doubleval(substr(@$_GET['cont'.$i],0,stripos(@$_GET['cont'.$i],'m')))*1024*1024;
//echo $size;
}
if(stripos($_GET['cont'.$i],"g"))
	{$size=doubleval(substr(@$_GET['cont'.$i],0,stripos(@$_GET['cont'.$i],'g')))*1024*1024*1024;}
if(stripos($_GET['cont'.$i],"t"))
	{$size=doubleval(substr(@$_GET['cont'.$i],0,stripos(@$_GET['cont'.$i],'t')))*1024*1024*1024*1024;
//echo $size;
}
$query[$i]   =   "SELECT   title, identifier ,ftp_site, publication_date,modification_date  FROM  dataset  where identifier in (select identifier from file  where file_size > ".@$size.")";}

$i++;
}
//echo $i;
$sql=$query[0];
for($j=1;$j<$i;$j++){
$sql=$sql.@$_GET['bool'.$j].@$query[$j];
}
$sql=$sql." order by identifier";
echo "<h2>The query semantics: </h2>".$sql;
$result   =   pg_query($sql)   or   die( 'Query   failed:   '   .   pg_last_error()); 
if(pg_num_rows($result)==0)
	{echo "<h2>NO RESULT YOU WANT</h2> ";}
else{

echo "<h2>Result:".pg_num_rows($result)." </h2><table border='1' cellpadding='5'><tr><th></th><th>Dataset_DOI</th><th>Title</th><th>Dataset type</th><th> Publication date</th><th>Modification date </th><th>Upload status</th><th>Ftp site</th></tr>";

while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 

	$qq="select dataset_type from datasettype where identifier like '".$line['identifier']."'";
    $rr  =   pg_query($qq)   or   die( 'Query   failed:   '   .   pg_last_error());
	$type="";
	while($ll= pg_fetch_array($rr,   null,   PGSQL_ASSOC)) 
	{$type=$type.", ".$ll['dataset_type'];}
	$type=substr($type,2);

echo '<tr><td><a href="info.php?doi='.$line['identifier'].'">REVIEW DATA</a></td><td width="110"> <a href="http://dx.doi.org/'.$line['identifier'].'">'.$line['identifier'].'</a></td><td width="300"> <a href="http://dx.doi.org/'.$line['identifier'].'">'.$line['title'].'</a></td><td>'.$type.'</td><td>'.$line['publication_date'].'</td><td>'.$line['modification_date'].'</td><td>'.$line['upload_status'].'</td><td><a href="'.$line['ftp_site'].'">FTP</a></td></tr>';
       
		}
        

echo "</table>";
pg_free_result($result); }


pg_close($dbconn); 
//echo "OK!";
?>
</body></html>