package head;

import java.util.Comparator;

public class PickingRequestComparator implements Comparator<PickingRequest> {
	
	@Override
	public int compare(PickingRequest pr1, PickingRequest pr2) {
		int x = pr1.getId();
		int y = pr2.getId();
		if(x < y) {
			return -1;
		} else if (x > y) {
			return 1;
		} else {
			return 0;
		}
	}
}
