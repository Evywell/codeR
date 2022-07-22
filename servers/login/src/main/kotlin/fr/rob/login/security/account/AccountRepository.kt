package fr.rob.login.security.account

import fr.rob.core.database.Connection
import fr.rob.core.database.closeCursor
import fr.rob.core.database.exception.InsertException
import fr.rob.core.database.getDateOrNull
import fr.rob.core.database.returnAndClose
import fr.rob.core.database.returnAndCloseWithCallback

class AccountRepository(private val db: Connection) : AccountRepositoryInterface {

    override fun byAccountId(accountId: Int): Account? {
        val stmt = db.createPreparedStatement(SEL_ACCOUNT_BY_USER_ID)!!

        stmt.setInt(1, accountId)
        db.execute(stmt)

        val rs = stmt.resultSet

        if (!rs.next()) {
            return returnAndClose(null, rs, stmt)
        }

        return returnAndCloseWithCallback(rs, stmt) {
            Account(
                rs.getInt(1),
                rs.getInt(2),
                rs.getBoolean(3),
                rs.getString(4),
                getDateOrNull(rs, 5),
                rs.getBoolean(6)
            )
        }
    }

    override fun insert(accountSkeleton: Account): Account {
        val stmt = db.createPreparedStatement(INS_ACCOUNT, true)!!

        stmt.setInt(1, accountSkeleton.accountGlobalId!!)
        stmt.setBoolean(2, accountSkeleton.isAdministrator)
        stmt.setString(3, accountSkeleton.name)

        db.executeUpdate(stmt)

        val generatedKeys = stmt.generatedKeys

        if (!generatedKeys.next()) {
            closeCursor(generatedKeys, stmt)

            throw InsertException("Cannot insert account $accountSkeleton")
        }

        return returnAndCloseWithCallback(generatedKeys, stmt) {
            Account(
                generatedKeys.getInt(1),
                accountSkeleton.accountGlobalId,
                accountSkeleton.isAdministrator,
                accountSkeleton.name
            )
        }
    }

    override fun updateName(account: Account, accountName: String) {
        val stmt = db.createPreparedStatement(UPD_ACCOUNT_NAME)!!

        stmt.setString(1, accountName)
        stmt.setInt(2, account.id!!)

        db.execute(stmt)
        stmt.close()
    }

    override fun lock(accountId: Int) {
        val stmt = db.createPreparedStatement(UPD_ACCOUNT_LOCK)!!

        stmt.setBoolean(1, true)
        stmt.setInt(2, accountId)

        db.execute(stmt)
        stmt.close()
    }

    companion object {
        const val SEL_ACCOUNT_BY_USER_ID =
            "SELECT id, user_id, is_administrator, name, banned_at, is_locked FROM accounts WHERE user_id = ?;"

        const val INS_ACCOUNT = "INSERT INTO accounts (user_id, is_administrator, name) VALUES (?, ?, ?);"

        const val UPD_ACCOUNT_NAME = "UPDATE accounts SET name = ? WHERE id = ?;"
        const val UPD_ACCOUNT_LOCK = "UPDATE accounts SET is_locked = ? WHERE id = ?;"
    }
}
