import java.util.Calendar;

public class MerchantDealTest {
	private static Calendar calendar = Calendar.getInstance();
	public static void main(String[] args){
		calendar.set(2016, 06, 01, 23, 00);
		long t1 = calendar.getTimeInMillis();
		calendar.set(2016, 06, 02, 2, 00);
		long t2 = calendar.getTimeInMillis();
		Deal deal1 = new Deal(t1, t2);
		calendar.set(2016, 06, 02, 1, 00);
		long t3 = calendar.getTimeInMillis();
		calendar.set(2016, 06, 02, 4, 00);
		long t4 = calendar.getTimeInMillis();
		calendar.set(2016, 06, 8, 1, 00);
		long t5 = calendar.getTimeInMillis();
		calendar.set(2016, 06, 9, 1, 00);
		long t6 = calendar.getTimeInMillis();
		calendar.set(2016, 07, 1, 1, 00);
		long t7 = calendar.getTimeInMillis();
		
		
		
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t3, "D1"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t3, "W1"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t3, "WD12345"));
		//System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t3, "WD67"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t3, "M1"));
		System.out.println("111--------------------");
		
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t4, "D1"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t4, "W1"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t4, "WD12345"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t4, "WD67"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t4, "M1"));
		System.out.println("222--------------------");
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t5, "D1"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t5, "W1"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t5, "WD12345"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t5, "WD67"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t5, "M1"));
		System.out.println("333--------------------");
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t6, "D1"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t6, "W1"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t6, "WD12345"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t6, "WD67"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t6, "M1"));
		System.out.println("444--------------------");
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t7, "D1"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t7, "W1"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t7, "WD12345"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t7, "WD67"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal1.startTime, deal1.endTime, t7, "M1"));
		
		//Normal test
		calendar.set(2016, 06, 2, 13, 00);
		long deal2start = calendar.getTimeInMillis();
		calendar.set(2016, 06, 2, 16, 00);
		long deal2end = calendar.getTimeInMillis();
		Deal deal2 = new Deal(deal2start, deal2end);
		calendar.set(2016, 06, 1, 14, 00);
		long t8 = calendar.getTimeInMillis();
		System.out.println("test2=============");
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal2.startTime, deal2.endTime, t8, "M1"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal2.startTime, deal2.endTime, t8, "D1"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal2.startTime, deal2.endTime, t8, "WD12345"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal2.startTime, deal2.endTime, t8, "W1"));
		
		calendar.set(2016, 06, 3, 14, 00);
		long t9 = calendar.getTimeInMillis();
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal2.startTime, deal2.endTime, t9, "M1"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal2.startTime, deal2.endTime, t9, "D1"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal2.startTime, deal2.endTime, t9, "WD12345"));
		System.out.println(!MerchantDealUtil.isDealValidForTimestamp(deal2.startTime, deal2.endTime, t9, "W1"));
		System.out.println(MerchantDealUtil.isDealValidForTimestamp(deal2.startTime, deal2.endTime, t9, "WD67"));
		/**/
		
	}
	public static class Deal{
		public long startTime;
		public long endTime;
		public Deal(long startTime, long endTime){
			this.startTime = startTime;
			this.endTime = endTime;
		}
	}
}
