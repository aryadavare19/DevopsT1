import com.workerfinder.restapi.service.WorkerService;
import com.workerfinder.restapi.service.WorkerServiceImp1;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            WorkerService service = new WorkerServiceImp1();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("WorkerService", service);
            System.out.println("✅ RMI Server running on port 1099...");
            System.out.println(service.getAllWorkers());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}