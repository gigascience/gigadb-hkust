<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
 <meta name="keywords" content="" />
 <meta name="description" content="" />
 <meta http-equiv="content-type" content="text/html; charset=utf-8" />
 <title>why not?</title> 
 </head><body>
<?php
echo "<h2>DATASET</h2><input type=\"button\" value=\"XML\" /><br />";
$dbconn   =   pg_connect( "host=192.168.171.46   dbname=GigaDB   user=jesse   password=1234") or   die( 'Could   not   connect:   '   .   pg_last_error()); 
$prefix=array("AE"=>"http://www.ebi.ac.uk/arrayexpress/experiments/","dbGaP"=>"http://www.ncbi.nlm.nih.gov/projects/gap/cgi-bin/study.cgi?study_id=","dbSNP"=>"http://www.ncbi.nlm.nih.gov/projects/SNP/","dbVar"=>"http://www.ncbi.nlm.nih.gov/dbvar/studies/","DDBJ"=>"http://www.ddbj.nig.ac.jp/","DOI"=>"http://dx.doi.org/","EGA"=>"https://www.ebi.ac.uk/ega/studies/","ENA"=>"http://www.ebi.ac.uk/ena/data/view/","GENBANK"=>"http://www.ncbi.nlm.nih.gov/nuccore/","GEO"=>"http://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=","PMID"=>"http://www.ncbi.nlm.nih.gov/pubmed/","PROJECT"=>"http://www.ncbi.nlm.nih.gov/bioproject?term=","SAMPLE"=>"http://www.ncbi.nlm.nih.gov/biosample?term=","SRA"=>"http://www.ncbi.nlm.nih.gov/sra?term=","TRACE"=>"http://www.ncbi.nlm.nih.gov/Traces/home/","http"=>"http:");


$query="select * from dataset where identifier='".$_GET['doi']."'" ;



$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
if (pg_num_rows($result)==0){
	echo "DO NOT EXIT";}
	else{

while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
echo "<b>Identifier </b>==>".$line['identifier']."<br/>";
echo "<b>Title </b>==>".$line['title']."<br/>";
echo "<b>Description </b>==><br />".str_replace("\n","<br />",$line['description'])."<br/>";
//echo '<b>Description </b>==><br /><textarea rows="30" cols="200">'.$line['description'].'</textarea><br/>';

echo "<b>Publisher </b>==>".$line['publisher']."<br/>";
echo "<b>Publication_date </b>==>".$line['publication_date']."<br/>";
echo "<b>Modification_date </b>==>".$line['modification_date']."<br/>";
echo "<b>Release_date </b>==>".$line['release_date']."<br/>";
echo '<b>Ftp_site </b>==><a href="'.$line['ftp_site'].'">'.$line['ftp_site'].'</a><br/>';
if(strlen($line['dataset_size'])<4){

echo "<b>Dataset_size </b>==>".$line['dataset_size']."<br/>";}
if(strlen($line['dataset_size'])<7&&strlen($line['dataset_size'])>3){
	echo "<b>Dataset_size </b>==>".round($line['dataset_size']/1000)." KB<br/>";}
if(strlen($line['dataset_size'])<10&&strlen($line['dataset_size'])>6){
	echo "<b>Dataset_size </b>==>".round($line['dataset_size']/1000000)." MB<br/>";}
	if(strlen($line['dataset_size'])<13&&strlen($line['dataset_size'])>9){
	echo "<b>Dataset_size </b>==>".round($line['dataset_size']/1000000000)." GB<br/>";}
	if(strlen($line['dataset_size'])<16&&strlen($line['dataset_size'])>12){
	echo "<b>Dataset_size </b>==>".round($line['dataset_size']/1000000000000)." TB<br/>";}
  }
  
pg_free_result($result);
$query="select   submitter_handle, 
    submitter_first_name,
	submitter_last_name,
    submitter_email, 
    submitter_affiliation, 
    submitter_password 
     from  submitter where submitter_email =(select submitter_email from dataset where identifier='".$_GET['doi']."')" ;
echo"<h4>SUBMITTER</h4>";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 

 while (list($key, $val) = each($line))
  {
  echo "<b>$key</b> ==>$val<br />";
  }
  }
  pg_free_result($result); 
$query="select  author_name from author where  identifier='".$_GET['doi']."'" ;
echo"<h4>AUTHOR LIST</h4>";
$author="";
$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 

  $author=$author.$line['author_name']."; ";
 
  }
  echo substr($author,0,strlen($author)-2);
  pg_free_result($result); 
$query="select  * from datasettype where  identifier='".$_GET['doi']."'" ;
echo"<h4>DATASET TYPE</h4>";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error());
$author="";
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 

  $author=$author.$line['dataset_type'].", ";
 
  }
  echo substr($author,0,strlen($author)-2);
pg_free_result($result);

$query="select  species.tax_id AS tax_id, common_name, sample_id, genbank_name,scientific_name,sample_attributes  from species, sample where sample.tax_id=species.tax_id and species.tax_id in (select tax_id from dataset_species where  identifier='".$_GET['doi']."') and sample_id in (select sample_id from dataset_sample where identifier='".$_GET['doi']."')" ;
echo"<h4>SAMPLE</h4>";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
if (pg_num_rows($result)==0)
{echo "NONE<br />";}
if (pg_num_rows($result)==1)
{
	while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
		echo '<b>tax_id </b>==><a href=" http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id='.$line['tax_id'].'">'.$line['tax_id'].'</a><br />';
		echo '<b>common_name </b>==>'.$line['common_name'].'<br />';
		
		if (stristr($line['sample_id'],"SAMPLE"))
		{echo '<b>sample_id </b>==><a href="http://www.ncbi.nlm.nih.gov/biosample?term='.substr($line['sample_id'],7).'">'.$line['sample_id'].'</a><br />';
		//echo "hi";
		}
		else{echo '<b>sample_id </b>==>'.$line['sample_id'].'<br />';}
	

	echo '<b>genbank_name </b>==>'.$line['genbank_name'].'<br />';
	echo '<b>scientific_name </b>==>'.$line['scientific_name'].'<br />';
	
	if($line['sample_attributes']){
	$attr=explode(",",$line['sample_attributes']);
	for($k=0;$k<count($attr)-1;$k++)
		{$at=explode("=",$attr[$k]);
		echo'$at[0]';
	//echo '<b>sample_attribute: '.$at[0].' </b>==>'.$at[1].'<br />';
	
	
		}
		//$at=explode("=\"",$attr[$k]);
	//	if(preg_match('/disease=/', $at[0]))
	//	{
	//	echo '<b>sample_attribute: '.$at[0].' </b><br />';
	//	}
	//	else if(preg_match('/disease=/', $at[0])&&preg_match('/DOID/', $at[0]))
	//	{
	//		$a=trim(eregi_replace("[^0-9]","",$at[0]));   
	//	echo '<b>sample_attribute:disease ==>DOID: <a href="http://www.ncbi.nlm.nih.gov/term='.$a.'">'.$a.'</a></b><br />';
		//}
	//	else
	//echo '<b>sample_attribute: '.$at[0].' </b>==>'.substr($at[1],0,strlen($at[1])-1).'<br />';
	}
}}
if (pg_num_rows($result)>1)
{
	
$i=1;
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC)){  if ($i==1){
echo "NO.".$i."<br />";}
else echo "<br />NO.".$i."<br />";
echo '<b>tax_id </b>==><a href=" http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id='.$line['tax_id'].'">'.$line['tax_id'].'</a><br />';
		echo '<b>common_name </b>==>'.$line['common_name'].'<br />';
		
		if (stristr($line['sample_id'],"SAMPLE"))
		{echo '<b>sample_id </b>==><a href="http://www.ncbi.nlm.nih.gov/biosample?term='.substr($line['sample_id'],7).'">'.$line['sample_id'].'</a><br />';
		//echo "hi";
		}
		else{echo '<b>sample_id </b>==>'.$line['sample_id'].'<br />';}
	

	echo '<b>genbank_name </b>==>'.$line['genbank_name'].'<br />';
	echo '<b>scientific_name </b>==>'.$line['scientific_name'].'<br />';
	
	$attr=explode("\",",$line['sample_attributes']);
	if($line['sample_attributes']){
	for($k=0;$k<count($attr)-1;$k++)
		{$at=explode("=\"",$attr[$k]);
	echo '<b>sample_attribute: '.$at[0].' </b>==>'.$at[1].'<br />';
		}
		$at=explode("=\"",$attr[$k]);
	echo '<b>sample_attribute: '.$at[0].' </b>==>'.substr($at[1],0,strlen($at[1])-1).'<br />';}
 $i++;}}
  pg_free_result($result); 

$query="select * from image where image_location= (select image_location from dataset where identifier='".$_GET['doi']."')" ;
echo"<h4>IMAGE</h4>";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
echo '<b>image_location</b> ==>'.$line['image_location'].'<br />';
echo '<b>image_tag</b> ==>'.$line['image_tag'].'<br />';
echo '<b>image_url</b> ==><a href="'.$line['image_url'].'">'.$line['image_url'].'</a><br />';
echo '<b>image_license</b> ==>'.$line['image_license'].'<br />';
echo '<b>image_photographer</b> ==>'.$line['image_photographer'].'<br />';
echo '<b>image_source </b> ==>'.$line['image_source'].'<br />';
  }

pg_free_result($result); 

$query="select  * from  manuscript where  manuscript_doi in ( select manuscript_doi from dataset_manuscript where  identifier='".$_GET['doi']."')" ;
echo"<h4>MANUSCRIPT</h4>";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error());
if (pg_num_rows($result)==0)
{echo "NONE<br />";}
if (pg_num_rows($result)==1)
{while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 

  echo '<b>manuscript </b>==><a href=" http://dx.doi.org/'.$line['manuscript_doi'].'">'.$line['manuscript_doi']."</a><br />";
  echo '<b>pmid </b>==><a href=" http://www.ncbi.nlm.nih.gov/pubmed/'.$line['pmid'].'">'.$line['pmid']."</a><br />";

  }}
if (pg_num_rows($result)>1){
	$i=1;
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
	if ($i==1){
echo "NO.".$i."<br />";}
else echo "<br />NO.".$i."<br />";
  echo '<b>maunscript </b>==><a href=" http://dx.doi.org/'.$line['manuscript_doi'].'">'.$line['manuscript_doi']."</a><br />";
  echo '<b>pmid </b>==><a href=" http://www.ncbi.nlm.nih.gov/pubmed/'.$line['pmid'].'">'.$line['pmid']."</a><br />";
  $i++;
  }}
  pg_free_result($result); 

$query="select * from project where project_url in (select project_url from dataset_project where identifier='".$_GET['doi']."')" ;
echo"<h4>PROJECT</h4>";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
if (pg_num_rows($result)==0)
{echo "NONE<br />";}
if (pg_num_rows($result)==1)
{while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
echo '<b>project_name</b>==>'.$line['project_name'].'<br />';
echo '<b>project_url</b>==><a href="'.$line['project_url'].'">'.$line['project_url'].'</a><br />';
echo '<b>project_image_location</b>==>'.$line['project_image_location'].'<br />';
}}
if (pg_num_rows($result)>1){
	$i=1;
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
	if ($i==1){
echo "NO.".$i."<br />";}
else echo "<br />NO.".$i."<br />";
echo '<b>project_name</b>==>'.$line['project_name'].'<br />';
echo '<b>project_url</b>==><a href="'.$line['project_url'].'">'.$line['project_url'].'</a><br />';
echo '<b>project_image_location</b>==>'.$line['project_image_location'].'<br />';
 $i++;
  }}
  pg_free_result($result); 
  
$query="select * from external_link where link_url in (select link_url from dataset_external_link where identifier='".$_GET['doi']."')" ;
echo"<h4>EXTERNAL_LINK</h4>";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
if (pg_num_rows($result)==0)
{echo "NONE<br />";}
if (pg_num_rows($result)==1)
{while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
echo '<b>link_url </b>==><a href="'.$line['link_url'].'">'.$line['link_url'].'</a><br />';
echo '<b>link_type </b>==>'.$line['link_type'].'<br />';
}}
if (pg_num_rows($result)>1){
	$i=1;
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
	if ($i==1){
echo "NO.".$i."<br />";}
else echo "<br />NO.".$i."<br />";
echo '<b>link_url </b>==><a href="'.$line['link_url'].'">'.$line['link_url'].'</a><br />';
echo '<b>link_type </b>==>'.$line['link_type'].'<br />';
 $i++;
}}
pg_free_result($result); 

$query="select ext_acc_link  from datasetext_acc_link  where  identifier='".$_GET['doi']."'" ;
echo"<h4>ACCESSION_LINK</h4>";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error());
if (pg_num_rows($result)==0)
{echo "NONE<br />";}
else{
$author="";
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
$acc=explode(":",$line['ext_acc_link']);

  $author=$author.'<a href="'.$prefix[$acc[0]].$acc[1].'">'.$line['ext_acc_link'].'</a>, ';
 
  }
  echo substr($author,0,strlen($author)-2);}
  pg_free_result($result);
  $query="select  ext_acc_mirror from datasetext_acc_mirror where  identifier='".$_GET['doi']."'" ;
echo"<h4>ACCESSION_MIRROR</h4>";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
if (pg_num_rows($result)==0)
{echo "NONE<br />";}else{
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
$acc=explode(":",$line['ext_acc_mirror']);

  echo '==><a href="'.$prefix[$acc[0]].$acc[1].'">'.$line['ext_acc_mirror']."</a><br />";
 
  }}
  pg_free_result($result); 
 $query="select  file_name ,  file_location , file_type ,   date_stamp,   file_format ,   file_extension ,   file_description ,    sample_id,    file_size  from file where  identifier='".$_GET['doi']."'" ;
 //$query=" select * from file where file_location in (select file_location from dataset_file where identifier='".$_GET['doi']."')";
echo"<h4>FILES</h4>";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 
$i=1;
while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
	echo "<br />NO.".$i."<br />";
while (list($key, $val) = each($line))
  {

if($key=='file_location')
	  {echo '<b>'.$key.'</b> ==><a href="'.$val.'">'.$val.'</a><br />';}
else{
	if($key!='file_size'){
  echo "<b>$key</b> ==>$val<br />";}
  else{
	if(strlen($val)<4){
    echo "<b>file_size </b>==>".$val."<br/>";}
if(strlen($val)<7&&strlen($val)>3){
	echo "<b>file_size </b>==>".round($val/1000)." KB<br/>";}
if(strlen($val)<10&&strlen($val)>6){
	echo "<b>file_size </b>==>".round($val/1000000)." MB<br/>";}
	if(strlen($val)<13&&strlen($val)>9){
	echo "<b>file_size </b>==>".round($val/1000000000)." GB<br/>";}
	if(strlen($val)<16&&strlen($val)>12){
	echo "<b>file_size </b>==>".round($val/1000000000000)." TB<br/>";}
  }
  }

  }
 $i++;
  }}
  pg_free_result($result);

  ?></body></html>