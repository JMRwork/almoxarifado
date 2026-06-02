import { useState, useEffect } from 'react';
import ProductList from './components/ProductList';
import ProductForm from './components/ProductForm';
import DeleteConfirmation from './components/DeleteConfirmation';
import './App.css';

function App() {
  const [produtos, setProdutos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingProduto, setEditingProduto] = useState(null);
  const [deletingId, setDeletingId] = useState(null);

  // Carregar produtos ao iniciar
  useEffect(() => {
    fetchProdutos();
  }, []);

  const fetchProdutos = async () => {
    setLoading(true);
    await fetch('http://localhost:8080/produtos')
      .then((res) => {
        if (!res.ok) {
          throw new Error('Erro ao carregar produtos');
        }

        return res.json();
      })
      .then((data) => {
        setError(null);
        setProdutos(data);
      })
      .catch((err) => {
        setError('Não foi possível carregar os produtos. Verifique se o backend está rodando.');
        console.error('Erro ao carregar produtos:', err);
      })
      .finally(() => setLoading(false));
  };

  const handleSave = async (produtoData) => {
    await fetch(`http://localhost:8080/produtos${editingProduto ? `/${editingProduto.id}` : ''}`, {
      method: editingProduto ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' },
      body: JSON.stringify(produtoData),
    })
      .then((res) => {
        if (!res.ok) {
          return res.json().then((data) => {
            throw new Error(data.error || 'Erro ao salvar produto');
          });
        }
        return res.json();
      })
      .then((data) => {
        setError(null);
        fetchProdutos();
        handleCloseForm();
      })
      .catch((err) => {
        setError('Não foi possível salvar o produto. Verifique os dados informados.');
        console.error('Erro ao salvar produto:', err);
      });
  };

  const handleDelete = async () => {
    await fetch(`http://localhost:8080/produtos/${deletingId}`, {
      method: 'DELETE',
    })
      .then((res) => {
        if (!res.ok) {
          return res.json().then((data) => {
            throw new Error(data.error || 'Erro ao excluir produto');
          });
        }
        return null;
      })
      .then(() => {
        setError(null);
        fetchProdutos();
        setDeletingId(null);
      })
      .catch((err) => {
        setError('Não foi possível excluir o produto. Tente novamente.');
        console.error('Erro ao excluir produto:', err);
      });
  };

  const handleEdit = (produto) => {
    setEditingProduto(produto);
    setShowForm(true);
  };

  const handleCloseForm = () => {
    setShowForm(false);
    setEditingProduto(null);
  };

  const handleOpenNew = () => {
    setEditingProduto(null);
    setShowForm(true);
  };

  return (
    <div className="container">
      <header>
        <h1>📦 Controle de Almoxarifado</h1>
        <p>Gestão de produtos do armazém</p>
      </header>

      <button className="add-button btn-primary" onClick={handleOpenNew}>
        + Novo Produto
      </button>

      {error && <div className="error-message">{error}</div>}

      {loading ? (
        <div className="loading">Carregando produtos...</div>
      ) : (
        <ProductList
          produtos={produtos}
          onEdit={handleEdit}
          onDelete={(id) => setDeletingId(id)}
        />
      )}

      {/* Modal de formulário (cadastro/edição) */}
      {showForm && (
        <ProductForm
          initialData={editingProduto}
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