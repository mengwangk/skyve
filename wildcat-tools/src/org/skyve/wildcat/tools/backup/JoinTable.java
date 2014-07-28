package org.skyve.wildcat.tools.backup;

import org.skyve.metadata.model.Attribute.AttributeType;

class JoinTable extends Table {
	String ownerTableName;

	JoinTable(String name, String ownerTableName) {
		super(name);
		this.ownerTableName = ownerTableName;

		fields.put("owner_id", AttributeType.text);
		fields.put("element_id", AttributeType.text);
	}
}
