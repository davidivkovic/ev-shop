import { get } from 'svelte/store'

import { getAllParts } from '$lib/api/shops'

/**
 * @type { import('./$types').PageLoad }
 */
export async function load() {
  const user = await import('$lib/stores/userStore').then(m => m.user)
  const parts = await getAllParts(get(user).shop.id)
  return { parts }
}