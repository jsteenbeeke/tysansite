package com.tysanclan.site.projectewok.beans.impl;

import com.jeroensteenbeeke.lux.TypedResult;
import com.tysanclan.site.myriad.importformat.MIDataSet;
import com.tysanclan.site.myriad.importformat.MIRank;
import com.tysanclan.site.myriad.importformat.MIUser;
import com.tysanclan.site.projectewok.beans.MyriadExportService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.LinkedList;

@Service
class MyriadExportServiceImpl implements MyriadExportService {
	@Autowired
	private UserDAO userDAO;

	@Override
	public TypedResult<File> export() {
		return TypedResult.attempt(() -> {
			File export = File.createTempFile("ewok", ".xml");
			MIDataSet dataSet = new MIDataSet();

			dataSet.setUsers(new LinkedList<>());

			for (User user : userDAO.findAll()) {
				MIUser miUser = new MIUser();
				miUser.setId(user.getId());
				miUser.setUsername(user.getUsername());
				miUser.setPassword(user.getArgon2hash());
				miUser.setMail(user.getEMail());
				miUser.setRank(MIRank.valueOf(user.getRank().name()));

				dataSet.getUsers().add(miUser);
			}

			JAXB.marshal(dataSet, export);
			return export;
		});
	}
}
