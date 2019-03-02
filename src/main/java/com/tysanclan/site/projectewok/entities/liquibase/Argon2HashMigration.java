package com.tysanclan.site.projectewok.entities.liquibase;

import com.jeroensteenbeeke.hyperion.password.argon2.Argon2PasswordHasher;
import com.tysanclan.site.projectewok.entities.User;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.DatabaseException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Argon2HashMigration implements CustomTaskChange {

	@Override
	public void execute(Database database) throws CustomChangeException {

		final JdbcConnection conn = (JdbcConnection) database.getConnection();
		try {
			PreparedStatement preparedStatement = conn
					.prepareStatement("select id, password from tuser");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				long id = resultSet.getLong("id");
				String oldPassword = resultSet.getString("password");

				String newHash = Argon2PasswordHasher
						.hashNewPassword(oldPassword.toCharArray())
						.withHashLength(User.KEY_LENGTH)
						.withIterations(User.ITERATIONS)
						.withPHCIssue9DefaultMemorySettings()
						.withPHCIssue9DefaultParallelism();

				preparedStatement = conn.prepareStatement(
						"update tuser set argon2hash = ?, legacyhash = true where id = ?");
				preparedStatement.setString(1, newHash);
				preparedStatement.setLong(2, id);

				preparedStatement.execute();
			}

		} catch (DatabaseException | SQLException e) {
			throw new CustomChangeException(e);
		}
	}

	@Override
	public String getConfirmationMessage() {
		return "Migrated existing weak hashes to Argon2";
	}

	@Override
	public void setUp() throws SetupException {

	}

	@Override
	public void setFileOpener(ResourceAccessor resourceAccessor) {

	}

	@Override
	public ValidationErrors validate(Database database) {
		return new ValidationErrors();
	}
}
