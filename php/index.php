<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
 <meta name="keywords" content="" />
 <meta name="description" content="" />
 <meta http-equiv="content-type" content="text/html; charset=utf-8" />
 <title>why not?</title> 
<style type="text/css"> 
  <!-- 
 body{background:#fff} 
 .Menu { 
  position:relative; 
  width:100%; 
  height:220px; 
  z-index:1; 
  background: #FFF; 
  border:1px solid #000; 
  margin-top:-100px; 
  display:none;

 } 
 .Menu2 { 
  position: absolute; 
  left:0; 
  top:0; 
  width:100%; 
  height:200px; 
  overflow-y:scroll;
  z-index:1; 
 } 
 .Menu2 ul{margin:0;padding:0} 
 .Menu2 ul li{width:100%;height:25px;line-height:25px;text-indent:15px; 
              border-bottom:1px dashed #ccc;color:#666;cursor:pointer; 
     change:expression( 
      this.onmouseover=function(){ 
        this.style.background="#F2F5EF"; 
      }, 
      this.onmouseout=function(){ 
        this.style.background=""; 
      } 
     ) 
    } 
input{width:auto} 
 .form{width:auto;height:auto;} 
 .form div{position:relative;top:0;left:0;margin-bottom:5px} 
 #List0,#List1,#List2,#List3,#List4,#List5,#List6,#List7,#List8,#List9{left:0px;top:93px;} 
 --> 
  </style> 
  </head> <body>
<script type="text/javascript">
function formReset()
  {
  document.getElementById("myForm").reset()
  }
 
var rowNum = 1; 
//���һ�� 
function AddRow() { 
var myTable = document.getElementById("myTable"); 
var newTr = myTable.insertRow(rowNum); 
var newTd1 = newTr.insertCell(0); 
newTd1.setAttribute("style", "vertical-align: top;"); 

newTd1.innerHTML = '<select name="bool'+rowNum+'"><option value=" intersect ">AND</option><option value=" union ">OR</option><option value=" except ">NOT</option></select> '; 
var newTd2 = newTr.insertCell(1); 
newTd2.setAttribute("style", "vertical-align: top;"); 
newTd2.innerHTML = '<select name="item'+rowNum+'" id="mySelect'+rowNum+'"  onchange="addLi('+rowNum+');showAndHide(\'List'+rowNum+'\',\'hide\');"><option value =1>All Fields</option><option value =6>Accession</option><option value =2>Author (last name, initials)</option><option value =3>Dataset_type</option><option value =16>Dataset_DOI</option><option value =5>Description</option><option value =8>External_link</option><option value =30>External_link_type</option><option value =9>File_description</option><option value =10>File_extension</option><option value =11>File_name</option><option value =12>File_size < </option><option value =38>File_size> </option><option value =13>File_format</option><option value =14>File_type</option><option value=4> Genbank_name</option><option value =17>Manuscript_DOI</option><option value =18>Modification_date before (YYYY/MM/DD)</option><option value =35>Modification_date after (YYYY/MM/DD)</option><option value =20>PMID </option><option value =21>Project_name</option><option value =22>Publication_date  before (YYYY/MM/DD)</option><option value =36>Publication_date  after (YYYY/MM/DD)</option><option value =23>Publisher</option><option value =25>Sample_id</option><option value=7>Scientific_name</option><option value =26>Submitter_affiliation</option><option value =27>Submitter_handle</option><option value =28>Tax_id</option><option value =29>Title</option></select>'; 
var newTd3 = newTr.insertCell(2); 
newTd3.setAttribute("align", "left"); 

newTd3.innerHTML = ' <div><input type="text" name="cont'+rowNum+'"  id="txt'+rowNum+'"  onClick="showAndHide(\'List'+rowNum+'\',\'show\');" > </div>     <div class="Menu" id="List'+rowNum+'">      <div class="Menu2">        <ul id="ol'+rowNum+'">	   </ul>         </div>  </div>'; 
var newTd4 = newTr.insertCell(3); 
newTd4.setAttribute("style", "vertical-align: top;"); 
newTd4.innerHTML = ' <input type="button" value="REMOVE" onclick="DeleteRow('+rowNum+')" />'; 

rowNum++; 
}  
function DeleteRow(num) { 
var myTable = document.getElementById("myTable"); 

myTable.deleteRow(num); 
 
}
 function showAndHide(obj,types){ 
	 if (types=="show")
	 {var x = document.getElementById("mySelect"+ obj.substring(4));
    var ix=x.selectedIndex;
	if(ix==3||ix==4||ix== 7 ||ix== 9 ||ix==13||ix== 14||ix== 15 ||ix==20 ||ix==23 ||ix==26 ||ix==25)
     var Layer=window.document.getElementById(obj); 
   
      Layer.style.display="block"; 
	 }else{
	
     var Layer=window.document.getElementById(obj); 
    
      Layer.style.display="none"; 
	
	 }
   } 
   function getValue(obj,str){ 
     var input=window.document.getElementById(obj); 
  input.value=str; 
   } 
  

 function addLi(n){
	
var x = document.getElementById("mySelect"+n);

var ix=x.selectedIndex;
var aEg = new Array([],[],[],["Epigenomic"
,"Genomic"
,"Transcriptomic"
],["10.5524/100001"
,"10.5524/100002"
,"10.5524/100003"
,"10.5524/100004"
,"10.5524/100005"
,"10.5524/100006"
,"10.5524/100007"
,"10.5524/100008"
,"10.5524/100009"
,"10.5524/100010"
,"10.5524/100011"
,"10.5524/100012"
,"10.5524/100013"
,"10.5524/100014"
,"10.5524/100015"
,"10.5524/100016"
,"10.5524/100017"
,"10.5524/100018"
,"10.5524/100019"
,"10.5524/100020"
,"10.5524/100021"
,"10.5524/100022"
,"10.5524/100023"
,"10.5524/100024"
,"10.5524/100025"
,"10.5524/100026"
,"10.5524/100027"
,"10.5524/100028"
,"10.5524/100032"
],[],[],["additional_information"
,"genome_browser"],[],[
	"agp"
,"annotation"
,"cdepth"
,"cds"
,"cfg"
,"chain"
,"chr"
,"chrM"
,"chrX"
,"chrY"
,"confirmed1_blast_ge100"
,"contig"
,"data"
,"depth"
,"doc"
,"fa"
,"fasta"
,"fastq"
,"fq"
,"geno_soap3"
,"gff"
,"haplotype"
,"ID"
,"ipr"
,"iprscan"
,"kegg"
,"length"
,"list"
,"maf"
,"name"
,"net"
,"pdf"
,"pep"
,"png"
,"PP"
,"qmap"
,"qual","rpkm"
,"scaffold"
,"scafSeq"
,"scafSeq_FG"
,"seq"
,"snp"
,"stat"
,"tab"
,"tar"
,"txt"
,"unknown"
,"updata"
,"vcf"
,"wego"
,"wig"
,"xls"
],[],[],[],["AGP"
,"CHAIN"
,"CONTIG"
,"EXCEL"
,"FASTA"
,"FASTQ"
,"GFF"
,"IPR"
,"KEGG"
,"MAF"
,"NET"
,"PDF"
,"PNG"
,"TAR"
,"TEXT"
,"unknown"
,"WEGO"
],["Alignments"
,"Annotation"
,"Coding sequence"
,"Genome assembly"
,"Genome sequence"
,"Genotyping data"
,"InDels"
,"Methylome data"
,"Other"
,"Protein sequence"
,"Readme"
,"SNPs"
,"SVs"
,"Transcriptome data"
,"Transcriptome sequence"
],["Adelie penguin"
,"Chinese hamster"
,"chiru"
,"crab-eating macaque"
,"emperor penguin"
,"Florida carpenter ant"
,"foxtail millet"
,"giant panda"
,"human"
,"Jerdon's jumping ant"
,"naked mole-rat"
,"Panamanian leafcutter ant"
,"pig"
,"pigeon pea"
,"pig roundworm"
,"polar bear"
,"potato"
,"Rhesus monkey"
,"Rock pigeon"
,"sheep"
,"sorghum "
],[],[],[],[],["1000 Genomes"
,"Genome 10K"
,"International Sheep Genetics Consortium"
,"Temasek LifeSciences Laboratory"
,"The Institute of Botany, The Chinese Academy of Sciences"
,"The Potato Genome"],[],[],["BGI Shenzhen"
,"GigaScience"],[],["Acromyrmex echinatior"
,"Ailuropoda melanoleuca"
,"Aptenodytes forsteri"
,"Ascaris suum"
,"Bombyx"
,"Brassica rapa subsp. pekinensis"
,"Cajanus cajan"
,"Camponotus floridanus"
,"Columba livia"
,"Cricetulus griseus"
,"Cucumis sativus var. sativus"
,"Escherichia coli"
,"Harpegnathos saltator"
,"Heterocephalus glaber"
,"Homo sapiens"
,"Macaca fascicularis"
,"Macaca mulatta"
,"Ovis aries"
,"Pantholops hodgsonii"
,"Pygoscelis adeliae"
,"Schistosoma haematobium"
,"Setaria italica"
,"Solanum tuberosum"
,"Sorghum bicolor"
,"Sus scrofa"
,"Ursus maritimus"
],["BGI"
,"CAAS"
,"CSIRO Livestock Industries"],[],[],[]
);

//var target = document.getElementById("Menu2");
var ol = document.getElementById("ol"+n);
//var   obj   =   document.getElementById( "ol1").childNodes; 
//for(var   i=0;   i <obj.length-1;i++) obj[i].removeNode(true); 

	var children = ol.childNodes;
	for( i=ol.childNodes.length-1;i>=0;i--){
		ol.removeChild(children[0]);}
	//var children = ol.childNodes;
	//ol.removeChild(children[0]);
	var ol = document.getElementById("ol"+n);
	
		for (j=0;j< aEg[ix].length;j++ )
		{var myLi = document.createElement("li");
   myLi.onmousedown=new Function("getValue('txt"+n+"','"+aEg[ix][j]+"');showAndHide('List"+n+"','hide')");
  myLi.innerHTML=aEg[ix][j]
  ol.appendChild(myLi);
		}

//var children = ol.childNodes;
//	ol.removeChild(children[0]);
}
</script>


<h2>Submit System</h2>
<form action="upload_file.php" method="post"
enctype="multipart/form-data">
<label for="file">Filename:</label>
<input type="file" name="file" id="file" /> 
<br />
<input type="submit" name="submit" value="Submit" />
</form>
------------------------------------------------------------------------------
<h2>Reserved Dataset</h2>
<script language=JavaScript>


function InputCheck(LoginForm)
{
		var reg = new RegExp("^10[\.]5524/([0-9]{6})$");
   if(!reg.test(LoginForm.username.value))
	{
 
    alert("Wrong DOI");
    LoginForm.username.focus();
    return (false);
  }
  return (true);
}


</script>
<form action="re.php" method="get" onSubmit="return InputCheck(this)">
Reserved DOI:
<input  id="username" type="text" name="re"  /> <br / >
Notes:
<input type="text" name="note"/><br />
<input type="submit"  value="Submit" />
</form>
------------------------------------------------------------------------------
<h2>Input-box for search</h2>
<form action="search1.php" method="get">
<div class="form"> 
<table id="myTable" > 
<tr> <td style="vertical-align: top;">
<input type="button" value="Add search" onClick="AddRow()" /></td><td style="vertical-align: top;">
<select name="item0" id="mySelect0" onChange="addLi(0);showAndHide('List0','hide');">
<option value =1>All Fields</option>
<option value =6>Accession</option>
<option value =2>Author (last name, initials)</option>
<option value =3>Dataset_type</option>
<option value =16>Dataset_DOI</option>
<option value =5>Description</option>
<option value =8>External_link</option>
<option value =30>External_link_type</option>
<option value =9>File_description</option>
<option value =10>File_extension</option>
<option value =11>File_name</option>
<option value =12>File_size < </option>
<option value =38>File_size > </option>
<option value =13>File_format </option>
<option value =14>File_type</option>
<option value=4> Genbank_name</option>
<option value =17>Manuscript_DOI</option>
<option value =18>Modification_date before (YYYY/MM/DD)</option>
<option value =35>Modification_date after (YYYY/MM/DD)</option>
<option value =20>PMID </option>
<option value =21>Project_name</option>
<option value =22>Publication_date  before (YYYY/MM/DD)</option>
<option value =36>Publication_date  after (YYYY/MM/DD)</option>
<option value =23>Publisher</option>
<option value =25>Sample_id</option>
<option value=7>Scientific_name</option>
<option value =26>Submitter_affiliation</option>
<option value =27>Submitter_handle</option>
<option value =28>Tax_id</option>
<option value =29>Title</option>


</select>
</td><td >


   <div><input type="text" name="cont0"  id="txt0" onClick="showAndHide('List0','show');"  > </div> 
     <!--�б�1--> 
     <div class="Menu" id="List0" > 
     <div class="Menu2" > 
       <ul id="ol0" >
	   </ul>
     </div> 
     </div> 
	
</td> 
<td>

</td>

  </tr></table>
</div>
 <input type="submit" value="Search" />

</form>


------------------------------------------------------------------------------
<h2>Check-box for search</h2>
<table cellpadding="10" ><tr><td style="vertical-align: top;">
<table border="1" >
<tr>
<th>Include</th>
<th>Exclude</th>

<th>Item</th>
</tr>
<tr>
<form id="myForm" action="search3.php" method="get">
<th colspan="3">Organism</th>
</tr>


<?php
echo '<input type="hidden" value="'.time().'" name="time">';
$dbconn   =   pg_connect( "host=192.168.171.46   dbname=GigaDB   user=jesse   password=1234") or   die( 'Could   not   connect:   '   .   pg_last_error()); 
$query   = "select  common_name, tax_id from species ORDER   BY    common_name ";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 

while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
echo '<tr><td><input type="radio" name="'.$line['tax_id'].'" value= "1" /></td><td><input type="radio" name="'.$line['tax_id'].'" value="2" /></td><td>'.$line['common_name'].'</td></tr>';
  
		}

pg_free_result($result); 


?>






<tr>
<th colspan="3">Dataset Type </th></tr>
<?php

$query   = "select distinct dataset_type from datasettype  ";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 

while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
echo '<tr><td><input type="radio" name="'.$line['dataset_type'].'" value= "1" /></td><td><input type="radio" name="'.$line['dataset_type'].'" value="2" /></td><td>'.$line['dataset_type'].'</td></tr>';
  
		}

pg_free_result($result); 


?>



<tr>

<th colspan="3">Publisher</th>
</tr>
<tr><td><input type="radio" name="BGI" value="1" /></td><td><input type="radio" name="BGI" value="2" /></td><td>BGI Shenzhen</td></tr>
<tr><td><input type="radio" name="GigaScience" value="1" /></td><td><input type="radio" name="GigaScience" value="2" /></td><td>GigaScience</td></tr>



<tr>
<th colspan="3">Project Name</th>
</tr>
<?php

$query   = "select  project_name from project";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 

while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
echo '<tr><td><input type="radio" name="'.str_replace(" ","",$line['project_name']).'" value= "1" /></td><td><input type="radio" name="'.str_replace(" ","",$line['project_name']).'" value="2" /></td><td>'.$line['project_name'].'</td></tr>';
  		}

pg_free_result($result); 


?>
<tr>
</table></td>
<td style="vertical-align: top;">
<table border="1">
<tr>
<th>Include</th>
<th>Exclude</th>

<th>Item</th>
</tr><tr>

<th colspan="3">File Type</th>
</tr>
<?php

$query   = "select distinct file_type from file ORDER   BY    file_type ";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 

while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
echo '<tr><td><input type="radio" name="'.str_replace(" ","",$line['file_type']).'" value= "1" /></td><td><input type="radio" name="'.str_replace(" ","",$line['file_type']).'" value="2" /></td><td>'.$line['file_type'].'</td></tr>';
  
		}
		?>
<tr>
<th colspan="3">File Format</th>
</tr>
<?php
$query   = "select distinct file_format from file ORDER   BY    file_format ";

$result   =   pg_query($query)   or   die( 'Query   failed:   '   .   pg_last_error()); 

while   ($line   =   pg_fetch_array($result,   null,   PGSQL_ASSOC))   { 
echo '<tr><td><input type="radio" name="'.$line['file_format'].'" value= "1" /></td><td><input type="radio" name="'.$line['file_format'].'" value="2" /></td><td>'.$line['file_format'].'</td></tr>';
  
		}
		?>


</table>
</td></tr></table>
<input type="button" onClick="formReset()" value="Reset"> 
<input type="submit" value="Search" />
</form>
</body>
</html>