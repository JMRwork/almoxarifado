const DeleteConfirmation = ({ onConfirm, onCancel }) => {
    return (
        <div className="modal-overlay">
            <div className="modal">
                <h3>Confirmar exclusão</h3>
                <p>Tem certeza que deseja excluir este produto?</p>
                <div className="form-actions">
                    <button className="btn-secondary" onClick={onCancel}>
                        Cancelar
                    </button>
                    <button className="btn-delete" onClick={onConfirm}>
                        Excluir
                    </button>
                </div>
            </div>
        </div>
    );
};

export default DeleteConfirmation;