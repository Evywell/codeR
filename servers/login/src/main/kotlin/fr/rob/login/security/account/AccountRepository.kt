package fr.rob.login.security.account

import fr.rob.core.database.Connection
import fr.rob.core.database.exception.InsertException
import fr.rob.core.database.returnAndClose

class AccountRepository(private val db: Connection) : AccountRepositoryInterface {

    override fun byUserId(userId: Int): Account? {
        val stmt = db.getPreparedStatement(SEL_ACCOUNT_BY_USER_ID)

        stmt.setInt(1, userId)
        stmt.execute()

        val rs = stmt.resultSet

        if (!rs.next()) {
            rs.close()

            return null
        }

        return returnAndClose(Account(rs.getInt(1), rs.getInt(2), rs.getBoolean(3), rs.getString(4)), rs)
    }

    override fun insert(accountSkeleton: Account): Account {
        val stmt = db.getPreparedStatement(INS_ACCOUNT, true)

        stmt.setInt(1, accountSkeleton.userId!!)
        stmt.setBoolean(2, accountSkeleton.isAdministrator)
        stmt.setString(3, accountSkeleton.name)

        stmt.executeUpdate()

        val generatedKeys = stmt.generatedKeys

        if (!generatedKeys.next()) {
            throw InsertException("Cannot insert account $accountSkeleton")
        }

        return returnAndClose(Account(
            generatedKeys.getInt(1),
            accountSkeleton.userId,
            accountSkeleton.isAdministrator,
            accountSkeleton.name
        ), generatedKeys)
    }

    override fun updateName(account: Account, accountName: String) {
        val stmt = db.getPreparedStatement(UPDATE_ACCOUNT_NAME)

        stmt.setString(1, accountName)
        stmt.setInt(2, account.id!!)

        stmt.execute()
    }

    companion object {
        const val SEL_ACCOUNT_BY_USER_ID =
            "SELECT id, user_id, is_administrator, name, banned_at FROM accounts WHERE user_id = ?;"

        const val INS_ACCOUNT = "INSERT INTO accounts (user_id, is_administrator, name) VALUES (?, ?, ?);"

        const val UPDATE_ACCOUNT_NAME = "UPDATE accounts SET name = ? WHERE id = ?;"
    }
}
