package fr.rob.login.security.account

import fr.rob.core.database.Connection
import fr.rob.core.database.exception.InsertException
import fr.rob.entities.AccountProto.Account

class AccountRepository(private val db: Connection) : AccountRepositoryInterface {

    override fun byUserId(userId: Int): Account? {
        val stmt = db.getPreparedStatement(SEL_ACCOUNT_BY_USER_ID)

        stmt.setInt(1, userId)
        stmt.execute()

        val rs = stmt.resultSet

        if (!rs.next()) {
            return null
        }

        return Account.newBuilder()
            .setId(rs.getInt(1))
            .setUserId(rs.getInt(2))
            .setIsAdministrator(rs.getBoolean(3))
            .build()
    }

    override fun insert(accountSkeleton: Account): Account {
        val stmt = db.getPreparedStatement(INS_ACCOUNT, true)

        stmt.setInt(1, accountSkeleton.userId)
        stmt.setBoolean(2, accountSkeleton.isAdministrator)

        stmt.executeUpdate()

        val generatedKeys = stmt.generatedKeys

        if (!generatedKeys.next()) {
            throw InsertException("Cannot insert account $accountSkeleton")
        }

        return Account.newBuilder()
            .setId(generatedKeys.getInt(1))
            .setIsAdministrator(accountSkeleton.isAdministrator)
            .setUserId(accountSkeleton.userId)
            .build()
    }

    companion object {
        const val SEL_ACCOUNT_BY_USER_ID =
            "SELECT id, user_id, is_administrator, banned_at FROM accounts WHERE user_id = ?;"

        const val INS_ACCOUNT = "INSERT INTO accounts (user_id, is_administrator) VALUES (?, ?);"
    }
}
