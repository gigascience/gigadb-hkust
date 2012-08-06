<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
 <meta name="keywords" content="" />
 <meta name="description" content="" />
 <meta http-equiv="content-type" content="text/html; charset=utf-8" />
 <title>why not?</title> 
 </head><body><?php
//echo "RUNNING TIME��".(time()-$_GET['time']);
$dbconn   =   pg_connect("host=192.168.171.46   dbname=GigaDB   user=jesse   password=1234") or   die( 'Could   not   connect:   '   .   pg_last_error()); 
 $sql_fd=""; $sql_fd1=""; $sql_spe="";$sql_spe1="";$sql_pro="";$sql_pro1="";$sql="";
 $sql_ty="";$sql_ty1="";$sql_pb="";$sql_pb1="";$sql_fl="";$sql_fl1="";$flag=0;

 //@#$%species
$query   = "select  tax_id from species";
$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
//$n_doi=pg_num_rows($result);
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
	
	if(@$_GET[urlencode($line['tax_id'])]==1)
		$sql_spe=$sql_spe.", ".$line['tax_id'];
	if(@$_GET[urlencode($line['tax_id'])]==2)
		$sql_spe1=$sql_spe1.", ".$line['tax_id'];
}
//echo $sql_spe;
pg_free_result($result); 
/*$query   = "select  identifier from dataset";
$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
//$n_doi=pg_num_rows($result);
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
	if(@$_GET[substr($line['identifier'],9)]==1)
		$sql_doi=$sql_doi.", '".$line['identifier']."'";
	if(@$_GET[substr($line['identifier'],9)]==2)
		$sql_doi1=$sql_doi1.", '".$line['identifier']."'";
}
pg_free_result($result); */


//@#$%project
$query   = "select  project_name, project_url from project";
$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
//$n_doi=pg_num_rows($result);
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
	if(@$_GET[str_replace(" ","",$line['project_name'])]==1)
		$sql_pro=$sql_pro.", '".$line['project_url']."'";
	if(@$_GET[str_replace(" ","",$line['project_name'])]==2)
		$sql_pro1=$sql_pro1.", '".$line['project_url']."'";
}
pg_free_result($result); 



$query   = "select distinct dataset_type from datasettype  ";
$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
//$n_doi=pg_num_rows($result);
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
	if(@$_GET[$line['dataset_type']]==1)
		$sql_ty=$sql_ty.", '".$line['dataset_type']."'";
	if(@$_GET[$line['dataset_type']]==2)
		$sql_ty1=$sql_ty1.", '".$line['dataset_type']."'";

}




$pub=array('BGI','GigaScience');
$pub1=array('BGI Shenzhen','GigaScience');

$i=0;
while($i<2)
{
	if(@$_GET[$pub[$i]]==1)
		$sql_pb=$sql_pb.", '".$pub1[$i]."'";
	if(@$_GET[$pub[$i]]==2)
		$sql_pb1=$sql_pb1.", '".$pub1[$i]."'";
$i++;
}
//echo $sql_pb1;





$query   = "select distinct file_type from file ORDER   BY    file_type ";
$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
//$n_doi=pg_num_rows($result);
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 

	if(@$_GET[str_replace(" ","",$line['file_type'])]==1)
		$sql_fl=$sql_fl.", '".$line['file_type']."'";
	if(@$_GET[str_replace(" ","",$line['file_type'])]==2)
		$sql_fl1=$sql_fl1.", '".$line['file_type']."'";
$i++;
}
$query   = "select distinct file_format from file ";
$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
//$n_doi=pg_num_rows($result);
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 

	if(@$_GET[$line['file_format']]==1)
		$sql_fd=$sql_fd.", '".$line['file_format']."'";
	if(@$_GET[$line['file_format']]==2)
		$sql_fd1=$sql_fd1.", '".$line['file_format']."'";
$i++;
}
if($sql_fd!=""){
	$sql_fd=substr($sql_fd,1);
$sql=$sql." intersect select identifier from file where file_format in(".$sql_fd.")";
}
if($sql_spe!=""){
$sql_spe=substr($sql_spe,1);
$sql=$sql." intersect select identifier from dataset_species where tax_id in ( ".$sql_spe.")";
}

if($sql_pro!=""){
$sql_doi=substr($sql_pro,1);
$sql=$sql." intersect select identifier from dataset_project where project_url in ( ".$sql_doi.")";
}
if($sql_ty!=""){
$sql_doi=substr($sql_ty,1);
$sql=$sql." intersect select identifier from datasettype where dataset_type in ( ".$sql_doi.")";
}
if($sql_pb!=""){
	$sql_doi=substr($sql_pb,1);
$sql=$sql." intersect select identifier from dataset where publisher in ( ".$sql_doi.")";
}
if($sql_fl!=""){
$sql_doi=substr($sql_fl,1);
$sql=$sql." intersect select identifier from  file where  file_type in( ".$sql_doi.")";
}
if($sql) $flag=1;
if($sql_spe1!=""){
	$sql_doi=substr($sql_spe1,1);
$sql=$sql." except select identifier from dataset_species where tax_id in ( ".$sql_doi.")";
}
if($sql_fd1!=""){
$sql_doi=substr($sql_fd1,1);
$sql=$sql." except  select identifier from file where  file_format in( ".$sql_doi.")";
}
if($sql_pro1!=""){
	$sql_doi=substr($sql_pro1,1);
$sql=$sql." except  select identifier from dataset_project where project_url in ( ".$sql_doi.")";
}
if($sql_ty1!=""){
$sql_doi=substr($sql_ty1,1);
$sql=$sql." except select identifier from datasettype where dataset_type in ( ".$sql_doi.")";
}
if($sql_pb1!=""){
	$sql_doi=substr($sql_pb1,1);
$sql=$sql." except select identifier from dataset where publisher in ( ".$sql_doi.")";
}
if($sql_fl1!=""){
$sql_doi=substr($sql_fl1,1);
$sql=$sql." except select identifier from file where  file_type in( ".$sql_doi.")";
}if(!$sql){
$query=" select  title, identifier ,ftp_site, publication_date,modification_date,upload_status from dataset";}
else{
if($flag==1){
$sql=substr($sql,10);
$query="select  title, identifier ,ftp_site, publication_date,modification_date,upload_status  FROM  dataset  where identifier in (".$sql.")";}
else{
	$sql=substr($sql,8);
$query="select  title, identifier ,ftp_site, publication_date,modification_date,upload_status  FROM  dataset  where identifier not in (".$sql.")";
}
}
$query=$query." order by identifier";
echo "<h2>The query semantics: </h2>".$query;
$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
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
if($line['publication_date']==null)
{
	$line['publication_date']="Waiting for published";
}
echo '<tr><td><a href="info.php?doi='.$line['identifier'].'">REVIEW DATA</a></td><td width="110"> <a href="http://dx.doi.org/'.$line['identifier'].'">'.$line['identifier'].'</a></td><td width="300"> <a href="http://dx.doi.org/'.$line['identifier'].'">'.$line['title'].'</a></td><td>'.$type.'</td><td>'.$line['publication_date'].'</td><td>'.$line['modification_date'].'</td><td>'.$line['upload_status'].'</td><td><a href="'.$line['ftp_site'].'">FTP</a></td></tr>';
     
		}
        
  echo "</table>";

pg_free_result($result); }

		pg_close($dbconn);
?>
</body></html>