const ApiTabs = ({ activeTab, onChange }) => {
    const tabs = [
        { key: 'items', label: 'Items' },
        { key: 'servicos', label: 'Serviços' },
    ];

    return (
        <div className="api-tabs" role="tablist" aria-label="APIs do almoxarifado">
            {tabs.map((tab) => (
                <button
                    key={tab.key}
                    type="button"
                    className={activeTab === tab.key ? 'tab-button active' : 'tab-button'}
                    onClick={() => onChange(tab.key)}
                >
                    {tab.label}
                </button>
            ))}
        </div>
    );
};

export default ApiTabs;
