package com.valmiraguiar.listo.feature.lists.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.valmiraguiar.listo.feature.lists.domain.model.ListCategory
import com.valmiraguiar.listo.feature.lists.domain.model.UnitOption

@Entity(
    tableName = "shopping_list_items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = ["list_id"],
            childColumns = ["list_owner_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["list_owner_id"])],
)
data class ShoppingListItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val id: Long = 0L,
    @ColumnInfo(name = "list_owner_id")
    val listId: Long,
    @ColumnInfo(name = "position")
    val position: Int,
    @ColumnInfo(name = "quantity")
    val quantity: String,
    @ColumnInfo(name = "unit")
    val unit: UnitOption,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "category")
    val category: ListCategory,
)
