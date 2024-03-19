package com.example.shopping_list_app

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ShoppingItem(val id:Int,var name :String,var quantity:Int,var isEditing:Boolean=false)
{

}
@Composable

fun ShoppingListApp()
{
    var sitem by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }

    var ShowDialog by remember { mutableStateOf(false) }

    var itemname by remember { mutableStateOf("")}
    var itemquantity by remember { mutableStateOf("")}

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Text(modifier = Modifier.padding(10.dp),text = "Developed By:\nSaurabh Maiti", fontSize = 20.sp, color = Color(0xFF000080))
        Button(
            onClick = {ShowDialog=true},
            modifier = Modifier.align(Alignment.CenterHorizontally)


        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        )
        {
            items(sitem)
            {
                item->
                if(item.isEditing)
                {
                    ShoppingItemEditor(item = item, onEditComplete ={
                        editedname ,editedQuantity->
                        sitem=sitem.map { it.copy(isEditing = false) }
                        val editedItem=sitem.find { it.id==item.id }
                        editedItem?.let {
                            it.name=editedname
                            it.quantity=editedQuantity
                        }
                    } )
                }

                else
                {
                    ShoppingListItem(item =item , onEditClick = {
                        sitem=sitem.map { it.copy(isEditing = it.id==item.id) }
                    }
                    , onDeleteClick = {sitem=sitem-item}
                    )
                }
            }
        }
        

    }

    if(ShowDialog)
    {
        AlertDialog(
            onDismissRequest = { ShowDialog=false},
            confirmButton = {
                      Row (modifier = Modifier
                          .fillMaxWidth()
                          .padding(8.dp),
                      horizontalArrangement = Arrangement.SpaceBetween
                      )
                      {
                          Button(onClick = {
                                if(itemname.isNotBlank())
                                {
                                    val newItem=ShoppingItem(id=sitem.size+1,
                                        name=itemname,
                                        quantity =itemquantity.toInt()

                                    )
                                    sitem=sitem+newItem
                                    ShowDialog=false
                                    itemname=""
                                }
                          })

                          {
                              Text(text = "Add")
                          }

                          Button(onClick = {ShowDialog=false}) {
                              Text(text = "Cancel")
                          }
                      }
            },
            title = {Text(text = "Add Shopping Items")},
            text = {
                Column {
                    OutlinedTextField(
                        value =itemname ,
                        onValueChange ={itemname=it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text(text = "Item Name")}
                        )
                    OutlinedTextField(
                        value =itemquantity ,
                        onValueChange ={itemquantity=it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text(text = "Item Quantity")}
                    )
                }
            }

        )
    }
}

@Composable
fun ShoppingItemEditor(item:ShoppingItem,onEditComplete:(String,Int)->Unit)
{
    var editedname by remember { mutableStateOf(item.name)}
    var editedQuantity by remember { mutableStateOf(item.quantity.toString())}
    var isEditing by remember { mutableStateOf(item.isEditing)}

    Row (modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp)
    , horizontalArrangement = Arrangement.SpaceEvenly
    ){

        Column {
            BasicTextField(value = editedname,
                onValueChange = {editedname=it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(8.dp)
            )
            BasicTextField(value = editedQuantity,
                onValueChange = {editedQuantity=it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(8.dp)
            )
        }

        Button(onClick = {
            isEditing=false
            onEditComplete(editedname,editedQuantity.toIntOrNull()?:1)
        }) {
            Text(text = "Save")
        }
    }

}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: ()->Unit,
    onDeleteClick: ()->Unit,
)
{
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color(0xFF000080)),
                shape = RoundedCornerShape(20)
            )
        ){
            Row {
                Text(text =item.name, modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.width(50.dp))
            Row {
                Text(text ="Qnty: ${item.quantity} ",modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.width(80.dp))
            Row (modifier = Modifier.padding(8.dp) )
            {
               /* IconButton(onClick = { onEditClick})
                {
                    Icon(imageVector = Icons.Default.Edit,contentDescription = null)
                }
                IconButton(onClick = { onDeleteClick })
                {
                    Icon(imageVector = Icons.Default.Delete,contentDescription = null)
                }
            */
            }
        }
}

