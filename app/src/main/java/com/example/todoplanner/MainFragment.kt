package com.example.todoplanner


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplanner.TodoAdapter.OnTodoClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment(), View.OnClickListener {



//        private var todoList = mutableListOf<ToDo>(
//            ToDo("Grocery1", "By groceries on the way home", "19/2/2019"),
//            ToDo("Grocery2", "By groceries on the way home", "19/2/2019"),
//            ToDo("Grocery3", "By groceries on the way home", "19/2/2019")
//    )

    val TAG = "Main Fragment Todo"

    lateinit var todoAdapter: TodoAdapter

    var navController: NavController? = null
    lateinit var newTodo: ToDo
    var position: Int = 0
    lateinit var todo: ToDo

    lateinit var todoViewModel: TodoViewModel

    lateinit var todoRecycler: RecyclerView

    lateinit var todoViewHolder: TodoViewHolder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        todoViewModel = activity?.run {
            ViewModelProviders.of(this)[TodoViewModel::class.java]
        }?: throw Exception("Invalid Activity")

        todoRecycler = view.findViewById(R.id.todo_recycler_view)
        todoAdapter = TodoAdapter(container!!.context)
        todoRecycler.adapter = todoAdapter
        todoRecycler.layoutManager = LinearLayoutManager(activity)

        todoViewModel.allTodos.observe(this, Observer { todo ->
            todoAdapter.submitList(todo)
        })

        setRecyclerViewItemTouchListener()

        todoViewHolder = TodoViewHolder(view)
//        todoViewHolder.deleteTodoItem(object : TodoViewHolder.OnTodoDeleteListener{
////            override fun deleteTodo() {
////                todoViewModel.delete(todoAdapter.getTodoAt(todoViewHolder.adapterPosition))
////            }
////
////        })

        todoAdapter.setOnTodoClickListener(object : OnTodoClickListener{
            override fun onTodoClick(position: Int) {
                todoViewModel.delete(todoAdapter.getTodoAt(position))
            }
        })

        return view
    }

    private fun setRecyclerViewItemTouchListener(){
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                todoViewModel.delete(todoAdapter.getTodoAt(position))
                Toast.makeText(activity, "Todo Deleted", Toast.LENGTH_LONG).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(todoRecycler)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<FloatingActionButton>(R.id.btn_create_todo).setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_create_todo -> navController!!.navigate(R.id.action_mainFragment_to_createTodo)
        }
    }



}
