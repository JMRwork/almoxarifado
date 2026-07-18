import { useState, useEffect } from 'react';
import ItemList from './components/ItemList';
import ItemForm from './components/ItemForm';
import DeleteConfirmation from './components/DeleteConfirmation';
import ItemHistory from './components/ItemHistory';
import './App.css';

function App() {
  const [itens, setItens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const [deletingId, setDeletingId] = useState(null);
  const [historyId, setHistoryId] = useState(null);

  // Carregar itens ao iniciar
  useEffect(() => {
    fetchItems();
  }, []);

  const fetchItems = async () => {
    setLoading(true);
    await fetch('http://localhost:8080/items')
      .then((res) => {
        if (!res.ok) {
          throw new Error('Erro ao carregar itens');
        }

        return res.json();
      })
      .then((data) => {
        setError(null);
        setItens(data);
      })
      .catch((err) => {
        setError('Não foi possível carregar os itens. Verifique se o backend está rodando.');
        console.error('Erro ao carregar itens:', err);
      })
      .finally(() => setLoading(false));
  };

  const handleSave = async (itemData) => {
    await fetch(`http://localhost:8080/items${editingItem ? `/${editingItem.id}` : ''}`, {
      method: editingItem ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' },
      body: JSON.stringify(itemData),
    })
      .then((res) => {
        if (!res.ok) {
          return res.json().then((data) => {
            throw new Error(data.error || 'Erro ao salvar item');
          });
        }
        return res.json();
      })
      .then((data) => {
        setError(null);
        fetchItems();
        handleCloseForm();
      })
      .catch((err) => {
        setError('Não foi possível salvar o item. Verifique os dados informados.');
        console.error('Erro ao salvar item:', err);
      });
  };

  const handleDelete = async () => {
    await fetch(`http://localhost:8080/items/${deletingId}`, {
      method: 'DELETE',
    })
      .then((res) => {
        if (!res.ok) {
          return res.json().then((data) => {
            throw new Error(data.error || 'Erro ao excluir item');
          });
        }
        return null;
      })
      .then(() => {
        setError(null);
        fetchItems();
        setDeletingId(null);
      })
      .catch((err) => {
        setError('Não foi possível excluir o item. Tente novamente.');
        console.error('Erro ao excluir item:', err);
      });
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    setShowForm(true);
  };

  const handleCloseForm = () => {
    setShowForm(false);
    setEditingItem(null);
  };

  const handleOpenNew = () => {
    setEditingItem(null);
    setShowForm(true);
  };

  const handleOpenHistory = (itemId) => {
    setHistoryId(itemId);
  };

  // Função para voltar da tela de histórico
  const handleBackFromHistory = () => {
    setHistoryId(null);
  };

  if (historyId) {
    return (
      <div class="container">
        <ItemHistory
          itemId={historyId} onBack={handleBackFromHistory}
        />
      </div>
    );
  }

  return (
    <div className="container">
      <header>
        <h1>📦 Controle de Almoxarifado</h1>
        <p>Gestão de itens do armazém</p>
      </header>

      <button className="add-button btn-primary" onClick={handleOpenNew}>
        + Novo Item
      </button>

      {error && <div className="error-message">{error}</div>}

      {loading ? (
        <div className="loading">Carregando itens...</div>
      ) : (
        <ItemList
          items={itens}
          onEdit={handleEdit}
          onDelete={(id) => setDeletingId(id)}
          onHistory={(id) => handleOpenHistory(id)}
        />
      )}

      {/* Modal de formulário (cadastro/edição) */}
      {showForm && (
        <ItemForm
          initialData={editingItem}
          onSave={handleSave}
          onCancel={handleCloseForm}
        />
      )}

      {/* Modal de confirmação de exclusão */}
      {deletingId && (
        <DeleteConfirmation
          onConfirm={handleDelete}
          onCancel={() => setDeletingId(null)}
        />
      )}
    </div>
  );
}

export default App;