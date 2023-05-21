<script>
  import Container from './Container.svelte'
  import userStore, { user } from '$lib/stores/userStore'
  import { page } from '$app/stores'
</script>

<div class="sticky h-16 w-full justify-center">
  <Container>
    <div class="flex gap-4">
      <a
        href="/"
        class="text-sm font-medium underline-offset-2"
        class:underline={$page.url.pathname === '/'}
      >
        Repair requests
      </a>
      {#if $user.role === 'repairman'}
        <a
          href="/parts"
          class="text-sm font-medium underline-offset-2"
          class:underline={$page.url.pathname === '/parts'}
        >
          Parts overview
        </a>
      {/if}
      <span class="ml-auto mr-1.5 text-sm">({$user.firstName} {$user.lastName})</span>
      <button type="button" class="p-0 text-sm font-medium" on:click={userStore.logout}>
        Log out
      </button>
    </div>
  </Container>
</div>
