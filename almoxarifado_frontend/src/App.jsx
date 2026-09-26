import { useState, useEffect } from 'react';
import ItemList from './components/ItemList';
import ItemForm from './components/ItemForm';
import ServicoList from './components/ServicoList';
import ServicoForm from './components/ServicoForm';
import DeleteConfirmation from './components/DeleteConfirmation';
import ItemHistory from './components/ItemHistory';
import ApiTabs from './components/ApiTabs';
import './App.css';

function App() {
  const GATEWAY_URL = import.meta.env.VITE_GATEWAY_URL || 'http://localhost:8080';
  const ITEMS_URL = `${GATEWAY_URL}/items-service`;
  const SERVICOS_URL = `${GATEWAY_URL}/servicos-service`;

  const [activeTab, setActiveTab] = useState('items');
  const [itens, setItens] = useState([]);
  const [servicos, setServicos] = useState([]);
  const [loading, setLoading] = useState({ items: true, servicos: true });
  const [error, setError] = useState({ items: '', servicos: '' });
  const [showForm, setShowForm] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const [deletingId, setDeletingId] = useState(null);
  const [historyId, setHistoryId] = useState(null);

  useEffect(() => {
    fetchItems();
    fetchServicos();
  }, []);

  const fetchItems = async () => {
    setLoading((prev) => ({ ...prev, items: true }));
    try {
      const response = await fetch(`${ITEMS_URL}/items`);
      if (!response.ok) throw new Error('Erro ao carregar itens');
      const data = await response.json();
      setItens(data);
      setError((prev) => ({ ...prev, items: '' }));
    } catch (err) {
      setError((prev) => ({ ...prev, items: 'Não foi possível carregar os itens. Verifique se o microserviço Items está rodando.' }));
      console.error('Erro ao carregar itens:', err);
    } finally {
      setLoading((prev) => ({ ...prev, items: false }));
    }
  };

  const fetchServicos = async () => {
    setLoading((prev) => ({ ...prev, servicos: true }));
    try {
      const response = await fetch(`${SERVICOS_URL}/servicos`);
      if (!response.ok) throw new Error('Erro ao carregar serviços');
      const data = await response.json();
      setServicos(data);
      setError((prev) => ({ ...prev, servicos: '' }));
    } catch (err) {
      setError((prev) => ({ ...prev, servicos: 'Não foi possível carregar os serviços. Verifique se o microserviço Serviços está rodando.' }));
      console.error('Erro ao carregar serviços:', err);
    } finally {
      setLoading((prev) => ({ ...prev, servicos: false }));
    }
  };

  const handleSave = async (itemData) => {
    const isServico = activeTab === 'servicos';
    const baseUrl = isServico ? SERVICOS_URL : ITEMS_URL;
    const endpoint = isServico ? '/servicos' : '/items';

    try {
      const response = await fetch(`${baseUrl}${endpoint}${editingItem ? `/${editingItem.id}` : ''}`, {
        method: editingItem ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(itemData),
      });

      if (!response.ok) {
        const payload = await response.json().catch(() => ({}));
        throw new Error(payload.error || payload.message || `Erro ao salvar ${isServico ? 'serviço' : 'item'}`);
      }

      if (isServico) {
        fetchItems();
        fetchServicos();
      } else {
        fetchItems();
      }

      handleCloseForm();
    } catch (err) {
      setError((prev) => ({
        ...prev,
        [activeTab]: `Não foi possível salvar ${isServico ? 'o serviço' : 'o item'}. Verifique os dados informados.`,
      }));
      console.error('Erro ao salvar:', err);
    }
  };

  const handleDelete = async () => {
    const isServico = activeTab === 'servicos';
    const baseUrl = isServico ? SERVICOS_URL : ITEMS_URL;
    const endpoint = isServico ? '/servicos' : '/items';

    try {
      const response = await fetch(`${baseUrl}${endpoint}/${deletingId}`, {
        method: 'DELETE',
      });

      if (!response.ok) {
        const payload = await response.json().catch(() => ({}));
        throw new Error(payload.error || payload.message || `Erro ao excluir ${isServico ? 'serviço' : 'item'}`);
      }

      if (isServico) {
        fetchServicos();
      } else {
        fetchItems();
      }

      setDeletingId(null);
    } catch (err) {
      setError((prev) => ({
        ...prev,
        [activeTab]: `Não foi possível excluir ${isServico ? 'o serviço' : 'o item'}. Tente novamente.`,
      }));
      console.error('Erro ao excluir:', err);
    }
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

  const handleBackFromHistory = () => {
    setHistoryId(null);
  };

  if (historyId) {
    return (
      <div className="container">
        <ItemHistory itemId={historyId} onBack={handleBackFromHistory} />
      </div>
    );
  }

  const currentTabLabel = activeTab === 'servicos' ? 'Serviços' : 'Itens';
  const currentItems = activeTab === 'servicos' ? servicos : itens;
  const currentLoading = loading[activeTab];
  const currentError = error[activeTab];

  return (
    <div className="container">
      <header>
        <h1>📦 Controle de Almoxarifado</h1>
        <p>Gestão de itens e serviços do armazém</p>
      </header>

      <ApiTabs activeTab={activeTab} onChange={setActiveTab} />

      <button className="add-button btn-primary" onClick={handleOpenNew}>
        + Novo {activeTab === 'servicos' ? 'Serviço' : 'Item'}
      </button>

      {currentError && <div className="error-message">{currentError}</div>}

      {currentLoading ? (
        <div className="loading">Carregando {currentTabLabel.toLowerCase()}...</div>
      ) : activeTab === 'servicos' ? (
        <ServicoList
          items={currentItems}
          onEdit={handleEdit}
          onDelete={(id) => setDeletingId(id)}
        />
      ) : (
        <ItemList
          items={currentItems}
          onEdit={handleEdit}
          onDelete={(id) => setDeletingId(id)}
          onHistory={(id) => handleOpenHistory(id)}
        />
      )}

      {showForm && (
        activeTab === 'servicos' ? (
          <ServicoForm
            initialData={editingItem}
            itens={itens}
            onSave={handleSave}
            onCancel={handleCloseForm}
          />
        ) : (
          <ItemForm
            initialData={editingItem}
            onSave={handleSave}
            onCancel={handleCloseForm}
          />
        )
      )}

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