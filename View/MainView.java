package View;

import javax.swing.JFrame;

import ClientController.ClientController;
import ClientController.CustomerViewController;
import ClientController.InventoryViewController;

public class MainView {
	public static void main (String[] args) {
		ClientController client1 = new ClientController("localhost", 8989);
        JFrame frame1 = new JFrame ("Customers");
        CustomerView view1 = new CustomerView();
        frame1.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame1.getContentPane().add (view1);
        frame1.pack();
        CustomerViewController control1 = new CustomerViewController(view1, client1);
        frame1.setVisible (true);
        
        ClientController client2 = new ClientController("localhost", 8989);
        JFrame frame2 = new JFrame ("Inventory");
        InventoryView view2 = new InventoryView();
        frame2.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame2.getContentPane().add (view2);
        frame2.pack();
        InventoryViewController control2 = new InventoryViewController(view2, client2);
        frame2.setVisible (true);
    }
}
