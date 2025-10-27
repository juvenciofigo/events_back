# Getting Started

api do aplicativo de eventos

## ToDo

[] Registo de Novo Utilizador ( Nome, email, password, telefone,)
[] Aceitação dos Termos de Serviço e Política de Privacidade
[] Seleciona o tipo de perfil:"Organizador de Eventos" ou "Prestador de Serviços"
[] Sistema envia email de confirmação(Opcional)

### Organizador

    [] Cadastra (Nome da empresa/profissional, user_id, profile_picture)
    [] Escolher preferências gerais de eventos(Opcional)

### Prestador de serviços

    [] Cadastra (Nome da empresa/profissional, user_id, profile_picture,logo,location_id,description)
    [] Cadastra informações para recebimento de pagamentos

#### Serviços

        [] Cadastra (service_supplier_id, category, description,price_base)
        [] Cadastra dias nao disponíel
        [] Cadastra um album (title, description,service_id)
        [] Cadastra as medias do album (media_type,service_album_id,file_url)

### Evento