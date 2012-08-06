/**
 * 
 */
package Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 王森洪
 *
 * @date 2012-3-7
 */
public class regrex {
	public static void main(String[] args) {
//		Pattern pattern=Pattern.compile("\\S+，\\S+\\（\\S+\\）");
// 
//		String temp= "&nbsp;生有自来知浩气英灵特钟衡岳；";
//		String regex = "(?i)[^a-zA-Z0-9\u4E00-\u9FA5]";
//		temp=temp.replaceAll(regex, "**");
//		System.out.println(temp);
//		Matcher m = pattern.matcher(temp);
//		if(m.matches())
//			System.out.println("asdfadsf");
//		else {
//			System.out.println("NO");
//		}
//	   String regrex="<option value=\"(\\S+)\" id=\"\\d+\">(\\S+)</option>";
////		Pattern pattern=Pattern.compile(regrex);
////		String string="<option value=\"北京市\" id=\"1\">北京市</option>";
////		Matcher matcher=pattern.matcher(strin
//	   regrex=";| +and +";
//	   String[] array="asdandsdfa;fasd    and asdfa; and ;asdfad".split(regrex) ;
//	   for(String s:array)
//		   System.out.println("*"+s+"*");
//	   if(true)
//		   return;
//		Pattern pattern=Pattern.compile(regrex);
//		String string="10.5524/100010"; 
//		Matcher matcher=pattern.matcher(string);
//		if(matcher.find() ){
//			System.out.println("true");
//		}
//		else{
//			System.out.println("false");
//		}
//		regrex="sall\\[\\d+\\]=new Array\\(\"(\\d+)\"[,|，]\"(\\S+)\".*";
////		regrex="sall\\[.*";
//		string="sall[0]=new Array(\"1\",\"不限\",\"不限\")";
//		pattern=Pattern.compile(regrex);
//		matcher=pattern.matcher(string);
//		System.out.println(string);
//		if(matcher.find()){
//			System.out.println("find");
//		}
		String content="This is the first version of the draft sheep (<em>Ovis aries</em>) reference genome (OAR v2.0)."+

"A single Texel ewe and a single Texel ram were independently sequenced by next-generation sequencing platforms.  The genome was assembled using a similar method to that used for the first de novo assembled genome using only next-generation sequencing platforms, the giant panda.  However, here more paired-end libraries and longer insert libraries were used. Firstly, using SOAPdenovo, the 75X Texel ewe Illumina reads were assembled de novo into contigs and scaffolds.  Then, 120X Illumina sequence from both the Texel ewe (BGI) and ram (Roslin) was used for gap filling so as to improve the assembly.  At this point, the N50 length of contig was 18 kb, and the N50 length of the assembled scaffolds was 1.1Mb, achieving a total length of 2.71 Gb and covering ~93% of the expected ~3Gb length of the sheep genome."+

"The CHORI-243 BAC library end sequences were used to generate a set of super-scaffolds with an N50 of 37 Mb.  Then a high density RH map and a linkage map with 39,042 SNP makers (derived from the Ovine SNP50 BeadChip) were used to anchor the super-scaffolds to chromosomes.  Several rounds of manual checking and final error correction were carried out using the end sequences of the BACs in the bovine CHORI-243 library and 454 mate pair sequence data derived from 8kb and 20kb insert libraries of the male Texel (BCM).  Ambiguous positions were resolved using the predicted location of the SNPs based on OARv1.0 and conserved synteny with the UMD3 bovine genome assembly.  For the final release version, OAR v2.0, 95% (2.57 Gb) of the total scaffold sequence was placed onto the 26 autosomes and the X chromosome.";
//		content.replaceAll("\u0a00", " ");
//		content=content.replaceAll("\\p{Zs}"," ");
		content=content.replaceAll("\\u00A0"," ");
		int count=0;
		for(int j=0;j<content.length();j++){
			if(content.charAt(j)=='\u00A0'){
				count++;
				System.out.println("non break space");
			}
		}
		System.out.println("count: "+count);
	}

	
}
