const apiUrl = "http://localhost:8080/tasks";

// Abre o modal para adicionar uma nova tarefa
function openModal() {
    document.getElementById("task-modal").style.display = "block";
}

// Fecha o modal
function closeModal() {
    document.getElementById("task-modal").style.display = "none";
}

// Submete o formulário para criar uma nova tarefa
document.getElementById("task-form").addEventListener("submit", function (e) {
    e.preventDefault();

    const task = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value,
        priority: document.getElementById("priority").value,
        dueDate: document.getElementById("dueDate").value,
    };

    // Enviar os dados para a API
    fetch(apiUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(task),
    })
        .then(response => response.json())
        .then(data => {
            console.log('Tarefa criada com sucesso', data);
            closeModal();
            loadTasks(); // Atualiza as tarefas após a criação
        })
        .catch(error => {
            console.error("Erro ao criar tarefa:", error);
        });
});

// Carrega todas as tarefas e as organiza nas colunas de acordo com o status
function loadTasks() {
    fetch(apiUrl)
        .then(response => response.json())
        .then(tasks => {
            const todoColumn = document.getElementById("todo-tasks");
            const inProgressColumn = document.getElementById("in-progress-tasks");
            const doneColumn = document.getElementById("done-tasks");

            todoColumn.innerHTML = '';
            inProgressColumn.innerHTML = '';
            doneColumn.innerHTML = '';

            tasks.forEach(task => {
                const taskElement = createTaskElement(task);
                if (task.status === 'TODO') {
                    todoColumn.appendChild(taskElement);
                } else if (task.status === 'IN_PROGRESS') {
                    inProgressColumn.appendChild(taskElement);
                } else if (task.status === 'DONE') {
                    doneColumn.appendChild(taskElement);
                }
            });
        })
        .catch(error => {
            console.error("Erro ao carregar tarefas:", error);
        });
}

// Cria o elemento HTML de uma tarefa
function createTaskElement(task) {
    const taskElement = document.createElement("div");
    taskElement.classList.add("task");
    taskElement.innerHTML = `
        <h3>${task.title}</h3>
        <p>${task.description}</p>
        <p><strong>Prioridade:</strong> ${task.priority}</p>
        <p><strong>Data Limite:</strong> ${task.dueDate}</p>
    `;
    return taskElement;
}

// Carrega as tarefas quando a página for carregada
window.onload = loadTasks;
