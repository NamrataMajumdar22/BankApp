
import java.sql.*;
import java.util.Scanner;

public class Spare {
public static Scanner sc= new Scanner(System.in);

static int ch;
static long acc_num;
static int amount;
static int ac_bal;
static int ac_trans_bal;
static int c_bal;

	public static void main(String[] args)throws Exception {
	     connect();
	      
	      
	        System.out.println("1. Enter 1 to deposit cash");
	    	System.out.println("2. Enter 2 to withdraw cash");
	    	System.out.println("3. Enter 3 to transfer money to another account");
	    	System.out.println("4. Enter 4 to check the account balance");
	    	System.out.println("5. Enter 5 to open a new account");
	 	   	System.out.println("Enter your choice: ");
	    	ch=sc.nextInt();
	    	
	    	switch(ch)
	    	{
	    	case 1: deposit();
	    			break;
	    	case 2: withdraw();
	    			break;
	    	case 3: transfer();
	    			break;
	    	case 4: balanceCheck();
	    	        break;
	    	
	    	case 5:openAccount();
			        break;
	    	
	    	default: System.out.println("Invalid choice...System will auto exit in few seconds...");
	    	         delayAndExit();
	        }

         }
	
	private static Connection connect() //Connect to the BANK.db database
	{
		 Connection c = null;
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c =DriverManager.getConnection("jdbc:sqlite:C:\\Users\\NAMRATA MAJUMDAR\\Desktop\\db\\BANK.db");
	         
	      } catch ( Exception e ) {
	         System.out.println(e);
	      }
	      return c;
	}
	
	
	public static void deposit() {
		getAccountNumber();
		 getAmount();
		 ac_bal=ac_bal+amount;  //Incrementing balance
		 balUpdate(ac_bal,acc_num);
		System.out.println("Current balance in your account is: "+getBal(acc_num));
		  delayAndExit();
	}
	
    public static void withdraw() {
    	getAccountNumber();
    	 getAmount();
    	 ac_bal=ac_bal-amount;  //Decrementing balance
    	 balUpdate(ac_bal,acc_num);
    	 System.out.println("Current balance in your account is: "+getBal(acc_num));
    	  delayAndExit();
	}

     public static void transfer() {
    	 getAccountNumber();
    	 
    	 System.out.println("Enter the account number to which you want to transfer the money");
    	long transf_acc=sc.nextLong();
    	try(Connection c=connect();
    			Statement stmt=c.createStatement();
    			ResultSet rs= stmt.executeQuery("SELECT acc_no, balance from DETAILS"))
    	{
    		while(rs.getInt("acc_no")==transf_acc)
    		{
    			ac_trans_bal=rs.getInt("Balance");    //getting balance of entered account
    		}
    	}
    	catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	 
    	 getAmount();
    	 ac_bal=ac_bal-amount;  //Decrementing balance
    	 balUpdate(ac_bal,acc_num);
    	 
    	 ac_trans_bal=ac_trans_bal+amount;
    	 balUpdate(ac_trans_bal,transf_acc);
    	 
    	 System.out.println("Current balance in your account is: "+getBal(acc_num));
    	  delayAndExit();
    }

     public static void balanceCheck() {
    	 getAccountNumber();
    	 System.out.println("Current balance in your account is: "+ac_bal);
    	  delayAndExit();
     }
     
     public static void openAccount() {
	   System.out.println("Enter your name");
	   String n1=sc.next();
	   System.out.println("Enter your 10 digit mobile number");
	   String p1=sc.next();
	   System.out.println("Enter your email id");
	   String e1=sc.next();
	   System.out.println("Enter the opening balance");
	   int bal1=sc.nextInt();
	   
	   insert(n1,p1,e1,bal1);
	   
	   delayAndExit();
	   
     }
     
     public static void insert(String name, String ph_no, String email_id, int balance) {
    	 try(Connection c= connect();
    			 PreparedStatement pstmt = c.prepareStatement("INSERT INTO details(name,ph_no,email_id,balance) VALUES(?,?,?,?)")	 )
    	 {
    		 pstmt.setString(1,name);
    		 pstmt.setString(2,ph_no);
    		 pstmt.setString(3,email_id);
    		 pstmt.setInt(4,balance);
    		 pstmt.executeUpdate();
    	 }
    	 catch (SQLException e) {
             System.out.println(e.getMessage());
         }
    	 
     }
	   
     
     
     public static void getAccountNumber()
     {
    	 System.out.println("Enter your account number"); //Receive the account number and pass it to the database
    	 acc_num=sc.nextLong();
    	try(Connection c=connect();
    			Statement stmt=c.createStatement();
    			ResultSet rs= stmt.executeQuery("SELECT acc_no, balance from DETAILS"))
    	{
    		while(rs.getInt("acc_no")==acc_num)
    		{
    			ac_bal=rs.getInt("Balance");    //getting balance of entered account
    		}
    	}
    	catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	
     }
     
     public static void getAmount()
     {
    	 System.out.println("Enter the amount"); //Receive the amount to be transacted and pass it to the database
         amount=sc.nextInt();
         
         
     }
     
     public static void balUpdate(int balo, long acnum)  //updating balance
     {
    	 try(Connection c=connect();
    			 PreparedStatement pstmt = c.prepareStatement("UPDATE details SET balance=?") ;
    	 Statement stmt=c.createStatement();
     			ResultSet rs= stmt.executeQuery("SELECT acc_no, balance from DETAILS"))
    	 {
    		 while(rs.getInt("acc_no")==acnum)
    		 {
    			 pstmt.setInt(1,balo);
    			 pstmt.executeUpdate();
    		 }
    	 }
    	 catch (SQLException e) {
             System.out.println(e.getMessage());
         }
     }
     
     public static int getBal(long ac_no)
     {
    	 
    	try(Connection c=connect();
    			Statement stmt=c.createStatement();
    			ResultSet rs= stmt.executeQuery("SELECT acc_no, balance from DETAILS"))
    	{
    		while(rs.getInt("acc_no")==ac_no)
    		{
    			c_bal=rs.getInt("Balance");    //getting balance of entered account
    		}
    	}
    	catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	return c_bal;
     }
     
    
     
     public static void delayAndExit()
     { 
    	 for(int i=0; i<10000000;i++)
    	 {
    		 //delay loop
    	 }
    	 System.exit(1);
     }
}

     
    
