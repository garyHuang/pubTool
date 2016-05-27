package org.gary.poi.util.xls;

import org.gary.comm.utils.Helper;
import org.gary.elasticsearch.hks.GIndex;
import org.gary.elasticsearch.hks.SynParts;

public class TestCopyDataToEs {
	
	public void synSolr(SynParts synParts){
		while(true){
			String[] materialcodes = synParts.getNextPartsMaterialcode() ;
			if(Helper.isNull(materialcodes)){
				break;
			}
			in:while(true){
				try{
					GIndex.addHksShopIndexs( materialcodes ) ;
					break in;
				}catch (Exception e) {
					e.printStackTrace();
					try {
						System.out.println(synParts.getPageId() + "-3分钟后重试");
						Thread.sleep(1000*60*3);
					} catch (Exception e1) {
					}
				}
			}
			//HksSolrIndex.addGysShopIndexs( materialcodes ) ;
		}
	}
	
	public static void main(String[] args) {
		final SynParts parts = new SynParts();
		final TestCopyDataToEs quartz = new TestCopyDataToEs();
		parts.setPageId( 1 ) ;
		
		for(int x=0;x<50;x++){
			new Thread(){
				@Override
				public void run() {
					quartz.synSolr( parts ) ; 
				}
			}.start();
		}
		//GIndex.addHksShopIndexs( new String[]{"15292861"} ) ;
	}
}
