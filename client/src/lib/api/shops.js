import { baseUrl, fetch } from '.'

const shopsUrl = baseUrl + '/repair-shops'

const addShop = async ({ name, address, brands }) =>
  fetch(`${shopsUrl}/add`, {
    method: 'POST',
    body: JSON.stringify({ name, address, brands })
  })

const getShops = async (brand) => {
  const response = await fetch(`${shopsUrl}?${new URLSearchParams({ brand }).toString()}`)

  return response.json()
}

export { addShop, getShops }
