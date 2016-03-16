package modules.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;
import org.skyve.domain.Bean;
import org.skyve.metadata.view.model.list.InMemoryFilter;
import org.skyve.wildcat.domain.MapBean;

public class InMemoryFilterTest extends AbstractH2Test {
	@Test
	@SuppressWarnings("static-method")
	public void testFilterStrings() throws Exception {
		List<Bean> beans = new ArrayList<>(4);

		Map<String, Object> map = new TreeMap<>();
		map.put("name", "Ted");
		MapBean bean = new MapBean("admin", "Contact", map);
		beans.add(bean);

		map = new TreeMap<>();
		map.put("name", "Fred");
		bean = new MapBean("admin", "Contact", map);
		beans.add(bean);

		map = new TreeMap<>();
		map.put("name", "Jed");
		bean = new MapBean("admin", "Contact", map);
		beans.add(bean);

		map = new TreeMap<>();
		map.put("name", "Ned");
		bean = new MapBean("admin", "Contact", map);
		beans.add(bean);

		map = new TreeMap<>();
		map.put("name", null);
		bean = new MapBean("admin", "Contact", map);
		beans.add(bean);

		InMemoryFilter f = new InMemoryFilter();
		f.addStartsWith("name", "Je");
		
		f.filter(beans);
		Assert.assertEquals("Filter result is wrong", 1, beans.size());
	}
}
