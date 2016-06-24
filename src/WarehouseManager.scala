import scala.collection.mutable.ArrayBuffer

/**
  * Created by Tevyn on 24/06/2016.
  */
object WarehouseManager extends Database{


  def main(args: Array[String]) {


    println("Whats your warehouse Op ID:")
    val wpID = scala.io.StdIn.readInt()

    println("What would you like to work on? Enter: " + "\n" + "1. Find and update order" + "\n" + "2. Update Stock")

    val decision = scala.io.StdIn.readInt()

    matchTest(decision)

    def matchTest(x: Int): Unit = x match {
      case 1 => orderList()
      case 2 => updateStock()
    }

    // recursive nested function print list of orders

    def orderList(): Unit = {
      for (s <- stock) {
        itemCheck(order)
        def itemCheck(list: ArrayBuffer[Orders]): Unit = {
          if (list.isEmpty) {
            return
          } else if (list.head.modelID == s.Model) {

            println("Order ID: " + list.head.ordID)
            println("Model ID: " + list.head.modelID)
            println("Product Type: " + s.productType)
            println("Quantity: " + list.head.quantity)
            println("Location Aisle: " + s.location_aisle + " Shelf: " + s.location)
            println("Porus Required: " + list.head.porus)
            println("Status: " + list.head.status)
            println("Warehouse Op working on Order: " + list.head.warehouseOp)
            println("\n")
            itemCheck(list.tail)
          } else {
            itemCheck(list.tail)
          }
        }

      }

    }


    println("Select the number of the order ID you want to update:")

    //read the order ID
    val ordRead = scala.io.StdIn.readInt()
    println("What would you like to update about this order:" +"\n" + "1. In Progress"+"\n" + "2. In Dispatched"+"\n" + "3. Delivered")
    val progress = scala.io.StdIn.readInt()

    selectOrder()

    //select order and update database
    def selectOrder(): Unit = {
      // need function to put items on hold
      for (o <- order) {
        if (o.ordID == ordRead) {
          itemPop(stock)
          def itemPop(itemlist: ArrayBuffer[Stock]): Unit = {
            if(itemlist.isEmpty){
              return
            }else if (progress == 1) {
              //run query to populate database to decrement value from quantity and increment hold
              //assign warehouse op ID
              val ne = itemlist.head.quantity
              val inPro = "in progress"
              updateOrders(wpID, inPro, ordRead)
              updateQuantity(ne, itemlist.head.Model)
              itemPop(itemlist.tail)
            } else if (progress == 2) {
              //dispatched
              val ne = itemlist.head.quantity
              val inPro = "dispatched"
              updateOrders(wpID, inPro, ordRead)
              dispatch(ne, itemlist.head.Model)
              itemPop(itemlist.tail)
            } else if (progress == 3) {
              //delivered
              val inPro = "delivered"
              updateOrders(wpID, inPro, ordRead)
              itemPop(itemlist.tail)
            }

          }
        } else {

        }


      }


    }
    if(progress == 1){
      routeAlgorithm(ordRead)

    }


    //calling the salesman algorithm
    def routeAlgorithm(orderID: Int): Unit ={
        for(o <- order ) {
        if(o.ordID == ordRead) {
          getLocation(stock)
        }
          def getLocation(locationList: ArrayBuffer[Stock]): Unit = {
        if(locationList.isEmpty){
          return
        }else if(o.modelID == locationList.head.Model){
          val aisle = locationList.head.location_aisle
         val h =  ShortestPath.Dijkstra(lookup, List((0.0, List("start"))), aisle, Set())
          println("Route and meters to collect " + locationList.head.productType + " at aisle " + aisle + " shelf " + locationList.head.location)
          println(h)
          getLocation(locationList.tail)
        }else{
          getLocation(locationList.tail)
        }
          }
        }



    }



  }



}
