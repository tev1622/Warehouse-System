import java.sql.{Connection, DriverManager, ResultSet}
import scala.collection.mutable.ArrayBuffer



trait Database {


  var stock = ArrayBuffer[Stock]()
  var order = ArrayBuffer[Orders]()
  var custList = ArrayBuffer[Customer]()
  var wareList = ArrayBuffer[WarehouseOp]()


    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost/mydb"
    val username = "root"
    val password = ""
    var connection: Connection = null
    connection = DriverManager.getConnection(url, username, password)


    stockResult()
    orderResult()
    custResults()
    wareResults()

  //get all from stock table
    def stockResult() {

      try {
        // make the connection
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)

        // create the statement, and run the select query
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM products")

        while (resultSet.next()) {
          val sr = new Stock(resultSet.getString("modelID"), resultSet.getString("productType"), resultSet.getString("quantity"), resultSet.getString("hold"),
            resultSet.getString("location_aisle"), resultSet.getString("location"))

          stock += sr
        }
      } catch {

        case e: Throwable => e.printStackTrace()

      }
    }
  //get all from order table
    def orderResult() {
      try {
        // make the connection
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)

        // create the statement, and run the select query
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM orders")
        while (resultSet.next()) {

          var ord = new Orders(resultSet.getInt("orderID"), resultSet.getString("modelID"), resultSet.getString("wareOpID"), resultSet.getString("customerID"),
            resultSet.getString("status"), resultSet.getString("porus"), resultSet.getString("quantity"))

          order += ord
        }
      } catch {
        case e: Throwable => e.printStackTrace
      }

      connection.close()
    }
  //get all from customer table

    def custResults(): Unit = {

      try {
        // make the connection
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)

        // create the statement, and run the select query
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM customer")
        while (resultSet.next()) {

          val cus = new Customer(resultSet.getString("customerID"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("address"))
          custList += cus
        }
      } catch {
        case e: Throwable => e.printStackTrace
      }

      connection.close()

    }
  //get all from warehoueop table

    def wareResults() {

      try {
        // make the connection
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)

        // create the statement, and run the select query
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM warehouseop")
        while (resultSet.next()) {

          val war = new WarehouseOp(resultSet.getString("wareOpID"), resultSet.getString("fname"), resultSet.getString("lname"), resultSet.getString("password"))
          wareList += war
        }
      } catch {
        case e: Throwable => e.printStackTrace
      }

      connection.close()

    }

  //update ordera
    def updateOrders(wid: Int, progress: String, oid: Int): Unit = {

      try {
        // make the connection
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)

        // create the statement, and run the select query
        val statement = connection.createStatement()

        statement.executeUpdate("UPDATE orders SET wareOpID='%s', status='%s' WHERE orderID='%s'".format(wid, progress, oid))
      } catch {
        case e: Throwable => e.printStackTrace
      }

      connection.close()

    }
//update quantity and hold
    def updateQuantity(quan: Any, modID: Any): Unit = {

      try {
        // make the connection
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)

        // create the statement, and run the select query
        val statement = connection.createStatement()
        statement.executeUpdate("UPDATE products SET quantity = quantity - '%s', hold = hold + '%s' WHERE modelID = '%s' and quantity > 0".format(quan, quan, modID))


      } catch{
        case e: Throwable =>
      }

    }
//decrementing from products
    def dispatch(quan: Any, modID: Any): Unit ={
      try {
        // make the connection
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)

        // create the statement, and run the select query
        val statement = connection.createStatement()
        statement.executeUpdate("UPDATE products SET hold = hold - '%s' WHERE modelID = '%s' ".format(quan, modID))


      } catch{
        case e: Throwable =>
      }

    }
    //update stock
    def updateStock() {

      println("Please Enter stock details ")
      println("Whats the Model Number: ")
      val model = scala.io.StdIn.readInt()
      println("Whats the Product Type: ")
      val prod = scala.io.StdIn.readLine()
      println("Whats the Quantity: ")
      val quantity = scala.io.StdIn.readLine()
      println("What is the location in the warehouse")
      val loc = scala.io.StdIn.readLine()


      try {
        // make the connection
        Class.forName(driver)

        // create the statement, and run the select query
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("INSERT INTO products (modelID, productType, location, quantity) VALUES ('%d', '%s', '%s', '%s')".format(model, prod, quantity, loc))

      }catch{

        case e => e.printStackTrace()

      }
    }


  //map of aisles and distance of products

  val lookup = Map(
    "start" -> List((7.0, "b"), (9.0, "c"), (25.0, "d"), (14.0, "f")),
    "b" -> List((10.0, "c"), (15.0, "d")),
    "c" -> List((11.0, "d"), (2.0, "f")),
    "d" -> List((6.0, "e")),
    "e" -> List((9.0, "f")),
    "f" -> Nil
  )


}
